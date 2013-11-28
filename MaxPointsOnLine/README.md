This problem is from http://oj.leetcode.com/problems/max-points-on-a-line/.


Ideas:

Try to solve the problem by set operations. There are following entities:

1) Point, given by input.


2) Line: Each line may contain several points. Each point can belongs to several lines.


3) Code logic:

   Step 1: Pick the first two Points to form a Line;

   Step 2: Loop from the 3rd Point to the last point. When loop to the n'th Point:

       2.1: Create an array onSameLine[0..n -2], the default value of each element of the array is False.

       2.2: Check whether the point belongs to any existing lines;

       2.3: For each Line this Point belongs to, add this Point to that Line, and mark the relevant onSameLine[i] as True for the other Points on that Line.
       
       2.4: After checked all the lines, for each Point with onSameLine[i] == false, create a new Line with that Point and the n'th Point.

   Step 3: After finished looping all the Points, check the Lines and compare the maximum point count among them. Done.
