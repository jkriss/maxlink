<html>
<head><title>jklabs :: MaxLink</title>

<link rel=stylesheet type="text/css" href="styles.css" title="standard">

<body>

<h1><a href="../">jklabs</a> :: MaxLink</h1>


<? 
$thispage = $_GET['page']; 
if ($thispage == "") $thispage = "about";

function isPage($pagename) {
	global $thispage;
	if ($pagename == "$thispage") echo "current ";
}

?>


<p>
<a href="?page=about" class="<? echo isPage("about") ?>menu">about</a>
<a href="?page=news" class="<? echo isPage("news") ?>menu">news</a>
<a href="?page=downloads" class="<? echo isPage("downloads") ?>menu">downloads</a>
<!-- <a href="?page=tutorials" class="<? echo isPage("tutorials") ?>menu">tutorials</a> -->
<a href="?page=reference" class="<? echo isPage("reference") ?>menu">reference</a>
<a href="?page=applications" class="<? echo isPage("applications") ?>menu">applications</a>
<a href="?page=faq" class="<? echo isPage("faq") ?>menu">faq</a>
<!-- <a href="?page=links" class="<? echo isPage("links") ?>menu">links</a> -->
<a href="?page=credits" class="<? echo isPage("credits") ?>menu">credits</a>
</p>

<div id="main">

<? 

include $thispage .".php";	
	
# the @ suppresses errors
 
include 'footer.php'; 

?>

</div>

</body>
</html>
