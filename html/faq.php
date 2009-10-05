<p class="question">
How does it work?
</p>

<p class="answer">
MaxLink uses the MultiSender and MultiReceiver classes from Max 4.5.  Messages are multicast over UDP to the local subnet using a namespace similar to OSC, and then decoded by the receiving objects.
</p>


<p class="question">
What's it good for?
</p>

<p class="answer">
MaxLink makes it really easy to send events and data from Processing to Max/MSP to trigger any sort of audio.  It's also easy to go the other way -- you can take controller data from Max and feed it to Processing to make reactive performance graphics.  Using the advanced networking features, it's even possible to take inputs and outputs from various objects and virtually plug them into each other using  a different computer on the subnet.
</p>


<p class="question">
Do I have to run my Processing sketches from Max?
</p>

<p class="answer">
Nope.  Each sketch can run as its own process anywhere on the local subnet.
</p>

<p class="question">
My sketch flickers when it starts up.  What's the deal?
</p>

<p class="answer">
This has to do with starting a network process in a seperate thread.  Once the flickering stops, it won't happen again until you restart the sketch.  This bug will be fixed in an upcoming release.
</p>

<p class="question">
Can I download the source?
</p>

<p class="answer">
Not yet.  Once I clean up the code a bit more, I'll post the source.
</p>