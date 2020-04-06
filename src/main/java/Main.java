import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<LocalTime> intersectedRange;
    private static List<List<LocalTime>> calendar1Booked;
    private static List<List<LocalTime>> calendar2Booked;
    private static List<List<LocalTime>> approvedMeetingIntervals;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");
    private static int meetingDurationInMinutes;

//  Sample Output  [['11:30','12:30'], ['15:00','16:00'], ['18:00':'18:30']]
// Actual good output [['11:30', '12:00'], ['15:00','16:00'], ['18:00':'18:30']]

    public static void main(String[] args) {

       String test = "[['09:00', '10:30'], ['12:00', '13:00'], ['16:00', '18:00']]";
       String test1 = test.substring(1);
       String test2 = test1.substring(0, test1.length() - 1);
       System.out.println(test2);

       DATE_TIME_FORMATTER.parse("12:30");


       //List<List<LocalTime>> meetingstest = LocalTime.parse();

//        setUp();
//
//        System.out.println(intersectedRange);
//        System.out.println(calendar1Booked);
//        System.out.println(calendar2Booked);
//
//        List<LocalTime> meetingInterval = new ArrayList<>();
//        meetingInterval.add(intersectedRange.get(0));
//        meetingInterval.add(meetingInterval.get(0).plus(meetingDurationInMinutes, ChronoUnit.MINUTES));
//
//        // While meeting ends before both schedules or at the end of both schedules
//        while(true)
//        {
//            LocalTime meetingStart = meetingInterval.get(0);
//            LocalTime meetingEnd = meetingInterval.get(1);
//            if(meetingEnd.isAfter(intersectedRange.get(1)))
//                break;
//
//            boolean is1Occ = isPersonOccupiedInInterval(calendar1Booked, meetingInterval);
//            boolean is2Occ = isPersonOccupiedInInterval(calendar2Booked, meetingInterval);
//            if(!is1Occ && !is2Occ)
//            {
//                List<LocalTime> newIntervalThatAvoidsReferences = new ArrayList<>();
//                newIntervalThatAvoidsReferences.add(meetingStart);
//                newIntervalThatAvoidsReferences.add(meetingEnd);
//                approvedMeetingIntervals.add(newIntervalThatAvoidsReferences);
//            }
//
//            meetingInterval.set(0, meetingInterval.get(0).plus(1, ChronoUnit.MINUTES));
//            meetingInterval.set(1, meetingInterval.get(1).plus(1, ChronoUnit.MINUTES));
//        }
//
//        System.out.println("Before parsing");
//        System.out.println(approvedMeetingIntervals);
//
//        System.out.println("After parsing");
//        parse(approvedMeetingIntervals);
//        System.out.println(approvedMeetingIntervals);
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
        LocalTime calendar1RangeBeginning = LocalTime.parse("09:00");
        LocalTime calendar1RangeEnding = LocalTime.parse("20:00");

        LocalTime calendar2RangeBeginning = LocalTime.parse("10:00");
        LocalTime calendar2RangeEnding = LocalTime.parse("18:30");

        List<LocalTime> calendar1Range = new ArrayList<>();
        calendar1Range.add(calendar1RangeBeginning);
        calendar1Range.add(calendar1RangeEnding);

        List<LocalTime> calendar2Range = new ArrayList<>();
        calendar2Range.add(calendar2RangeBeginning);
        calendar2Range.add(calendar2RangeEnding);

        intersectedRange = computeIntersectedRange(calendar1Range, calendar2Range);

        setUpCalendar1();
        setUpCalendar2();

        meetingDurationInMinutes = 30;

        approvedMeetingIntervals = new ArrayList<>();
    }



    private static void setUpCalendar2()
    {
        calendar2Booked = new ArrayList<>();

        List<LocalTime> c2Interval1 = new ArrayList<>();
        c2Interval1.add(LocalTime.parse("10:00"));
        c2Interval1.add(LocalTime.parse("11:30"));

        List<LocalTime> c2Interval2 = new ArrayList<>();
        c2Interval2.add(LocalTime.parse("12:30"));
        c2Interval2.add(LocalTime.parse("14:30"));

        List<LocalTime> c2Interval3 = new ArrayList<>();
        c2Interval3.add(LocalTime.parse("14:30"));
        c2Interval3.add(LocalTime.parse("15:00"));

        List<LocalTime> c2Interval4 = new ArrayList<>();
        c2Interval4.add(LocalTime.parse("16:00"));
        c2Interval4.add(LocalTime.parse("17:00"));

        calendar2Booked.add(c2Interval1);
        calendar2Booked.add(c2Interval2);
        calendar2Booked.add(c2Interval3);
        calendar2Booked.add(c2Interval4);
    }


    private static void setUpCalendar1()
    {
        calendar1Booked = new ArrayList<>();

        List<LocalTime> c1Interval1 = new ArrayList<>();
        c1Interval1.add(LocalTime.parse("09:00"));
        c1Interval1.add(LocalTime.parse("10:30"));

        List<LocalTime> c1Interval2 = new ArrayList<>();
        c1Interval2.add(LocalTime.parse("12:00"));
        c1Interval2.add(LocalTime.parse("13:00"));

        List<LocalTime> c1Interval3 = new ArrayList<>();
        c1Interval3.add(LocalTime.parse("16:00"));
        c1Interval3.add(LocalTime.parse("18:00"));

        calendar1Booked.add(c1Interval1);
        calendar1Booked.add(c1Interval2);
        calendar1Booked.add(c1Interval3);
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
