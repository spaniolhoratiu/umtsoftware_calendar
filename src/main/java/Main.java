import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static List<LocalTime> intersectedRange;
    private static List<LocalTime> calendar1Range;
    private static List<LocalTime> calendar2Range;
    private static String calendar1RangeString;
    private static String calendar2RangeString;
    private static String bookedCalendar1String;
    private static String bookedCalendar2String;
    private static String meetingString;
    private static List<List<LocalTime>> calendar1Booked;
    private static List<List<LocalTime>> calendar2Booked;
    private static List<List<LocalTime>> approvedMeetingIntervals;
    private static int meetingDurationInMinutes;

//  Sample Output  [['11:30','12:30'], ['15:00','16:00'], ['18:00':'18:30']]
//  Actual good output(because the first person is occupied [['11:30', '12:00'], ['15:00','16:00'], ['18:00':'18:30']]

    public static void main(String[] args) {

        bookedCalendar1String = "booked calendar1: [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00']]";
        bookedCalendar2String = "booked calendar2: [['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00'], ['16:00','17:00']]";
        calendar1RangeString = "calendar1 range limits: ['9:00','20:00']";
        calendar2RangeString = "calendar2 range limits: ['10:00','18:30']";
        meetingString = "Meeting Time Minutes: 30";

        setUp();

        computeAllPossibleMeetingTimes();

//        System.out.println("Before parsing");
//        System.out.println(approvedMeetingIntervals);
//
//        System.out.println("After parsing");
        parse(approvedMeetingIntervals);
        System.out.println(approvedMeetingIntervals);
    }

    private static void computeAllPossibleMeetingTimes()
    {
        List<LocalTime> meetingInterval = new ArrayList<>();
        meetingInterval.add(intersectedRange.get(0));
        meetingInterval.add(meetingInterval.get(0).plus(meetingDurationInMinutes, ChronoUnit.MINUTES));

        // While meeting ends before both schedules or at the end of both schedules
        while(true)
        {
            LocalTime meetingStart = meetingInterval.get(0);
            LocalTime meetingEnd = meetingInterval.get(1);
            if(meetingEnd.isAfter(intersectedRange.get(1)))
                break;

            boolean is1Occ = isPersonOccupiedInInterval(calendar1Booked, meetingInterval);
            boolean is2Occ = isPersonOccupiedInInterval(calendar2Booked, meetingInterval);
            if(!is1Occ && !is2Occ)
            {
                List<LocalTime> newIntervalThatAvoidsReferences = new ArrayList<>();
                newIntervalThatAvoidsReferences.add(meetingStart);
                newIntervalThatAvoidsReferences.add(meetingEnd);
                approvedMeetingIntervals.add(newIntervalThatAvoidsReferences);
            }

            meetingInterval.set(0, meetingInterval.get(0).plus(1, ChronoUnit.MINUTES));
            meetingInterval.set(1, meetingInterval.get(1).plus(1, ChronoUnit.MINUTES));
        }
    }


    private static void parse(List<List<LocalTime>> meetingIntervals)
    {
        boolean noModificationsNeeded = false;
        do {
            for (int i = 0; i < meetingIntervals.size() - 1; i++)
            {
                List<LocalTime> currentMeetingInterval = meetingIntervals.get(i);
                List<LocalTime> nextMeetingInterval = meetingIntervals.get(i + 1);

                if (currentMeetingInterval.get(0).plus(1, ChronoUnit.MINUTES).equals(nextMeetingInterval.get(0)) || currentMeetingInterval.get(1).plus(1, ChronoUnit.MINUTES).equals(nextMeetingInterval.get(1)) || currentMeetingInterval.get(1).equals(nextMeetingInterval.get(0)))
                {
                    currentMeetingInterval.set(1, nextMeetingInterval.get(1));
                    nextMeetingInterval.set(0, LocalTime.parse("00:00"));
                    nextMeetingInterval.set(1, LocalTime.parse("00:00"));
                    noModificationsNeeded = false;
                    meetingIntervals.removeIf(interval -> interval.get(0).equals(LocalTime.parse("00:00")) && interval.get(1).equals(LocalTime.parse("00:00")));
                    break;
                }

                noModificationsNeeded = true;
            }

        } while (!noModificationsNeeded);

    }



    private static boolean isPersonOccupiedInInterval(List<List<LocalTime>> calendarBooked, List<LocalTime> timeInterval)
    {
        LocalTime startInterval = timeInterval.get(0);
        LocalTime endInterval = timeInterval.get(1);

        for(List<LocalTime> currentBookedTimeInterval : calendarBooked)
        {
            LocalTime currentStartBookedInterval = currentBookedTimeInterval.get(0);
            LocalTime currentEndBookedInterval = currentBookedTimeInterval.get(1);

            // If meeting starts at the same time with a booked activity
            if(startInterval.equals(currentStartBookedInterval))
            {
                return true;
            }

            // If meeting is within the current activity
            if(startInterval.isAfter(currentStartBookedInterval) && endInterval.isBefore(currentEndBookedInterval))
            {
                return true;
            }

            // If meeting overlaps current activity(at start)
            if(startInterval.isBefore(currentStartBookedInterval) && endInterval.isAfter(currentStartBookedInterval))
            {
                return true;
            }

            // If meeting overlaps current activity(at end)
            if(startInterval.isBefore(currentEndBookedInterval) && endInterval.isAfter(currentEndBookedInterval))
            {
                return true;
            }

            // If meeting starts while a current activity is happening
            if(startInterval.isAfter(currentStartBookedInterval) && startInterval.isBefore(currentEndBookedInterval))
            {
                return true;
            }

        }
        return false;
    }

    private static void setUp()
    {
        calendar1Range = new ArrayList<>();
        calendar2Range = new ArrayList<>();
        setUpRange(calendar1RangeString, 1);
        setUpRange(calendar2RangeString, 2);

        intersectedRange = computeIntersectedRange(calendar1Range, calendar2Range);

        calendar2Booked = new ArrayList<>();
        calendar1Booked = new ArrayList<>();
        setUpCalendar(bookedCalendar1String, 1);
        setUpCalendar(bookedCalendar2String, 2);

        setUpMeetingTime();

        approvedMeetingIntervals = new ArrayList<>();

    }

    private static void setUpMeetingTime()
    {
        List<String> splitMeetingString = Arrays.asList(meetingString.split(": "));
        meetingDurationInMinutes = Integer.parseInt(splitMeetingString.get(1));
    }


    private static void setUpRange(String calendarRangeString, int whichRange)
    {
//        calendar1RangeString = "calendar1 range limits: ['9:00','20:00']";
        List<String> aux = Arrays.asList(calendarRangeString.split(": "));
        // ['9:00','20:00']
        calendarRangeString = aux.get(1);

        // '9:00','20:00'
        calendarRangeString = calendarRangeString.substring(1);
        calendarRangeString = calendarRangeString.substring(0, calendarRangeString.length() - 1);

        // '09:00' '20:00'
        List<String> splitString = Arrays.asList(calendarRangeString.split(","));
        String firstTime = splitString.get(0).substring(1);
        firstTime = firstTime.substring(0, firstTime.length() - 1);
        String secondTime = splitString.get(1).substring(1);
        secondTime = secondTime.substring(0, secondTime.length() - 1);

        if(firstTime.length() == 4)
            firstTime = "0" + firstTime;
        if(secondTime.length() == 4)
            secondTime = "0" + secondTime;

        if(whichRange == 1)
        {
            calendar1Range.add(LocalTime.parse(firstTime));
            calendar1Range.add(LocalTime.parse(secondTime));
        }
        else
        {
            calendar2Range.add(LocalTime.parse(firstTime));
            calendar2Range.add(LocalTime.parse(secondTime));
        }

    }


    private static void setUpCalendar(String bookedCalendarString, int whichCalendar)
    {
        //Split at ": " and take the right part
        List<String> aux = Arrays.asList(bookedCalendarString.split(": "));
        bookedCalendarString = aux.get(1);
        bookedCalendarString = bookedCalendarString.substring(1);
        bookedCalendarString = bookedCalendarString.substring(0, bookedCalendarString.length() - 1);

        List<String> tokenization = Arrays.asList(bookedCalendarString.split(", "));
        for(String s : tokenization) // all time have 6 chars and always from 2-7 and 10-15
        {
            // ['9:00','10:00'] -> '9:00','10:00'
            s = s.substring(1);
            s = s.substring(0, s.length() - 1);
            // '9:00' -> 9:00 separated by '10:00' -> 10:00
            List<String> splitS = Arrays.asList(s.split(","));
            String firstTime = splitS.get(0).substring(1);
            firstTime = firstTime.substring(0, firstTime.length() - 1);
            String secondTime = splitS.get(1).substring(1);
            secondTime = secondTime.substring(0, secondTime.length() - 1);

            // 9:00 -> 09:00
            List<LocalTime> interval = new ArrayList<>();
            if(firstTime.length() == 4)
                firstTime = "0" + firstTime;
            if(secondTime.length() == 4)
                secondTime = "0" + secondTime;

            interval.add(LocalTime.parse(firstTime));
            interval.add(LocalTime.parse(secondTime));
            if(whichCalendar == 1)
                calendar1Booked.add(interval);
            else calendar2Booked.add(interval);
        }

    }


    private static List<LocalTime> computeIntersectedRange(List<LocalTime> l1, List<LocalTime> l2)
    {
        List<LocalTime> dst = new ArrayList<>();
        //Beginning
        if(l1.get(0).isAfter(l2.get(0)))
        {
            dst.add(l1.get(0));
        }else
            dst.add(l2.get(0));

        //Ending
        if(l1.get(1).isBefore(l2.get(1)))
        {
            dst.add(l1.get(1));
        }
        else
            dst.add(l2.get(1));

        return dst;
    }

}
