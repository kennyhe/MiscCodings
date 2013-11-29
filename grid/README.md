#Content Addressable Network (CAN) Simulation

A Java implemention of content addressable network (CAN) for cloud computing, using p2p with network proximity.

For project requirements description, please refer to the p1.pdf.

The program read the input data from stdin.

The most difficult part of this program is about how to split the regions into two parts when adding a node, and how to combine the adjacent regions when removing a node. It needs some spacial imagination skills.

##How to run:
<pre>make
java CAN
or
java CAN < input data file
</pre>
