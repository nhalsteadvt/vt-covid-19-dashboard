# vt-covid-19-dashboard
Prerequisites: 
  You must have Java 8 installed and properly configured in order for this project to run by the script given.
  This is because it relies on the JavaFX library, which is available automatically through Java 8.
Projection description:
  This will parse a file of copied covid-19 data from the Virginia Tech dashboard.
  This project was done to better calculate trends and better graph the data.

Data folder:
  This contains two .txt files
  dashboard.txt:
    This is the file used if a single argument is not supplied.
    Leave this file untouched.
  url.txt
    This file is unused by the program itself.
    It a URL that (to date) links to the dashboard where the data is (manually) copied to dashboard.txt
    It is recommended to copy and paste the data from the table on that site into a separate .txt file.
    Then, pass its file location as an argument to Runner.

Special notes:
  Within dashboard.txt, there is a data anomaly.
  On 08/29, there were said to be 64 positive results and yet 16 tests.
  For this reason, the findMaxPercentage function in the Entry class is modified.
  It has a statement to ignore and entries with an impossible (>100%) percentage.
