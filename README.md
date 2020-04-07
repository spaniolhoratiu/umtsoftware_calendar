# umtsoftware_calendar
Modul de gandire pentru problema:
• Pentru parsarea string-urilor, am lasat comentarii in cod(e cam stufos).
• Pentru a gasi fiecare meeting acceptabil de ambele persoane:
  • Am calculat intervalul intersectat al ambelor persoane(9:00-20:00 si 10:00-18:00 devine 10:00-18:00)
  • De la inceputul intervalului intersectat am generat fiecare meeting posibil pana la finalul intervalului, incrementand cu 1 minut fata de meetingul trecut
  • Pentru fiecare meeting am verificat daca ambele persoane sunt libere. Daca da, meetingul este bagat in lista approvedMeetingIntervals
  • Am modificat lista approvedMeetingIntervals in urmatorul fel:
	• Parcurgand lista, daca meetingul urmator este la 1 minut distanta fata de cel curent sau daca meetingul curent se termina la acelasi timp in care incepe urmatorul meeting, atunci:
	• [15:00, 15:30], [15:01, 15:31] devine [15:00, 15:31], [00:00, 00:00], se sterge lista avand "00:00" ca start point si end point, si se reia parcurgerea de la inceput
	
Astfel:

Before parsing
[[11:30, 12:00], [15:00, 15:30], [15:01, 15:31], [15:02, 15:32], [15:03, 15:33], [15:04, 15:34], [15:05, 15:35], [15:06, 15:36], [15:07, 15:37], [15:08, 15:38], [15:09, 15:39], [15:10, 15:40], [15:11, 15:41], [15:12, 15:42], [15:13, 15:43], [15:14, 15:44], [15:15, 15:45], [15:16, 15:46], [15:17, 15:47], [15:18, 15:48], [15:19, 15:49], [15:20, 15:50], [15:21, 15:51], [15:22, 15:52], [15:23, 15:53], [15:24, 15:54], [15:25, 15:55], [15:26, 15:56], [15:27, 15:57], [15:28, 15:58], [15:29, 15:59], [15:30, 16:00], [18:00, 18:30]]
After parsing
[[11:30, 12:00], [15:00, 16:00], [18:00, 18:30]]