Hidden (HIgh-Dimensional Data Exploration and Navigation)
by Takayuki Itoh (Ochanomizu University, Japan, E-mail:itot(at)is.ocha.ac.jp)

Implementation of high-dimensional data visualization technique published as follows:
T. Itoh, A. Kumar, K. Klein, J. Kim, High-Dimensional Data Visualization by Interactive Construction of Low-Dimensional Parallel Coordinate Plots, Journal of Visual Languages and Computing, Vol. 43, pp. 1-13, 2017.

Running on Java 1.8 or newer, mdsj.jar, and JOGL (Java binging OpenGL) 2.2.
(Remark that unrecommended packages (java.awt, java.swing, etc.) are used in the code.)

How to run:
1) Invoke "ocha.itolab.hidden.applet.numdim.NumdimViewer".
2) Press the button "File Open" and select a data file.


Data file format (Please prepare as CSV files):

(Numeric or Category),(Numeric or Category),(Numeric or Category)...
(nameA),(nameB),(nameC)...
(value1A),(value1B),(value1C)...
(value2A),(value2B),(value2C)...
...

* "Numeric" columns must be filled by real values.
* "Category" columns must be filled by categorical values.
