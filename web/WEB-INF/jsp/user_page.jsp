<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>
<%@include file="includes/navigation.jsp" %>

<!-- User information -->
<div class="container">
	<div class="row-fluid">
		<div class="span12 well">
			<div class="span2">
				<a href="#" >
					<img src="http://placehold.it/128x128" alt="">
				</a>
			</div>
			<div class="span8">
				<p>Username</p>
				<span class="badge badge-warning">8 messages</span> <span class=" badge badge-info">15 followers</span>
			</div>
			<div class="span2">
				<button class="btn btn-success pull-right"> <i class="icon-plus"></i> Follow</button>
			</div>
		</div>
	</div>
 	
 	<!-- Post a message -->
 	<div class="row-fluid">
 		
 		<form id="post-message-form" action="/messages/" method="post">
 			<fieldset>
				<textarea class="input-block-level" rows="3" placeholder="Напишіть повідомлення..."></textarea>
 				
 				<button type="submit" class="btn">Написати</button>
 			</fieldset>
 		</form>
 	</div>
 	
 	<!-- Message feed -->
	<div class="row-fluid">
	    <div class="row-fluid">
	    	<div class="span1 text-center">
	    		<a href="#"><img src="http://placehold.it/42x42" class="img-circle"></a>
	    	</div>
  		    <div class="span9">
			    'Yes, we went to school in the sea, though you mayn't believe it—'
			    'I never said I didn't!' interrupted Alice.
			    'You did,' said the Mock Turtle.
		    </div>
		    <div class="span2 timebadge">
		    	<span class="badge pull-right"><i class="icon-time"></i> 2012-08-02 20:47:04</span>
		    </div>
    	    
	    </div>
	    <hr/>
	    <div class="row-fluid">
	    	<div class="span1 text-center">
	    		<a href="#"><img src="http://placehold.it/42x42" class="img-circle"></a>
	    	</div>
  		    <div class="span9">
  		    	I am bound to Tahiti for more men
			</div>
		    <div class="span2 timebadge">
		    	<span class="badge pull-right"><i class="icon-time"></i> 2012-08-02 20:47:04</span>
		    </div>
		    
	    </div>
		<hr/>
	    <div class="row-fluid">
	    	<div class="span1 text-center">
	    		<a href="#"><img src="http://placehold.it/42x42" class="img-circle"></a>
	    	</div>
  		    <div class="span9">
  		    	Very good. Let me board you a moment—I come in peace.
  		    </div>
	    	<div class="span2 timebadge">
		    	<span class="badge pull-right"><i class="icon-time"></i> 2012-08-02 20:47:04</span>
		    </div>
	    	
	    </div>
		
		<hr/>
	    
	</div><!-- .row -->
	
</div><!-- .container -->

<%@include file="includes/footer.jsp" %>