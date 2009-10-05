
<p>
You can use the MaxLink class and the included Max objects to handle communication in a number of ways. Here are some examples.</p>

<div class="altsetup">
<h3>Processing within Max</h3>
<img src="images/p5inmax.png">

<p>
Using the jk.p5 object, you can launch Processing sketches from within Max.
</p>
</div>

<div class="altsetup">
<h3>Processing alongside Max</h3>
<img src="images/p5andmax.png">
<p>
Alternatively, you can run Processing sketches from the Processing IDE or launch them as separate Java apps. Sketches tend to run more smoothly in this way (they don't always appreciate running within the Max process).
</p>
</div>

<div class="altsetup">
<h3>Processing and Max on different computers</h3>
<img src="images/twocomputers.png">
<p>
You can devote a computer to each app, since the objects communicate using UDP multicast.  This configuration is particularly useful if there are multiple people contributing to a performance.
</p>
</div>

<div class="altsetup">
<h3>A Java process, Processing, and Max communicating over the subnet</h3>
<img src="images/threecomputers.png">
<p>
Why stop at two computers? Also, the MaxLink class can be included in any Java process, not just Processing sketches. The jk.link object acts as an input/output proxy for each app. In this setup, the computer running Max can connect outlets from the Java process to inlets in the Processing sketch. Using jk.scout, it's even possible to query the local subnet for communicating objects, then wire up their inlets and outlets as desired.
</p>
</div>