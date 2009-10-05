<h2>MaxLink class functions 0.3b</h2>

<p class="reference">
<span class="function">MaxLink(Object parent, String name)</span><br/>
<code>MaxLink link = new MaxLink(this, "test_sketch");</code>
<br/>Constructor.  Takes a reference to the parent object (the sketch) and a reference name (usually the name of the sketch).
</p>

<p class="reference">
<span class="function">declareInlet(String varName)</span><br/>
<code>link.declareInlet("xPosition");</code><br/>
Creates an inlet in the corresponding Max object that maps to the specified variable. The variable must be declared as public (i.e. <code class="inline">public float varName;</code>).
</p>

<p class="reference">
<span class="function">declareInlet(String varName, String setterFunctionName)</span><br/>
<code>link.declareInlet("xPosition", "setX");</code><br/>
Creates an inlet in the corresponding Max object and sets a function to call when input is received in the inlet.  The setter function must be public, and have a single parameter of type equal to varName's class. (That is, if xPosition is a float, the setter function should be <code class="inline">public void setX(float newX)</code>.)
</p>

<p class="reference">
<span class="function">
	declareMaxFunction(String functionName) 
</span><br/>
<code>link.declareMaxFunction("printVars")</code><br/>
Declares a Processing function to be accessible from Max.  The function must be public, and not have any parameters.
</p>

<p class="reference">
<span class="function">ouput(int i) </span><br/>
<code>link.output(42);</code><br/>
Sends an int out of the default (leftmost) outlet.
</p>

<p class="reference">
<span class="function">ouput(float f) </span><br/>
<code>link.output(27.33);</code><br/>
Sends a float out of the default (leftmost) outlet.
</p>

<p class="reference">
<span class="function">ouput(String s) </span><br/>
<code>link.output("hi max!");</code><br/>
Sends a String out of the default (leftmost) outlet.
</p>

<p class="reference">
<span class="function">ouput(int outletNumber, int i) </span><br/>
<code>link.output(0, 42);</code><br/>
Sends an int out of the specified outlet.
</p>

<p class="reference">
<span class="function">ouput(int outletNumber, float f) </span><br/>
<code>link.output(1, 27.33);</code><br/>
Sends a float out of the specified outlet.
</p>

<p class="reference">
<span class="function">ouput(int outletNumber, String s) </span><br/>
<code>link.output(2, "hi max!");</code><br/>
Sends a String out of the specified outlet.
</p>
