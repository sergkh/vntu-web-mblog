
/*
 * First approach: Function called from HTML by adding onclick attribute.
 */
function manageUser(linkEl, userId) {
    var link = $(linkEl); // wrap DOM element in jQuery object

    var request = {
        "userId" : userId,
        "action" : link.data("action")
    };

    $.post(
        baseUrl + '/admin',
        request,
        function() {
            // invert action
            var newAction = (link.data("action") == 'block') ? 'unblock' : 'block';
            // set new text
            var newText = (newAction == 'block') ? 'Заблокувати' : 'Розблокувати';

            link.data("action", newAction).html(newText);
        },
        'text'
    );


   return false;
}

$(function() {
    /*
     * Second approach: find elements from DOM and add click listener.
     * 'data-xxx' attributes can be added to pass element specific data.
     */
    $('#users-table tr').each(function() {
        var row = $(this);  // wrap DOM element in jQuery object

        row.on('click', 'a.permissions-link', function(evt) {
            // block link triggering
            evt.preventDefault();

            var link = $(this);

            var req = {
                "userId" : row.val("user"),
                "action" : link.data("action"),
                "permission" : link.data("permission")
            };

            $.post(baseUrl + '/admin', req, function() {
                var cell = row.find('td.permissions');

                if("addPermission" == req.action) {
                    cell.append('<span class="badge">' + req.permission + '</span>');
                    link.data("action", "removePermission")
                        .html('<i class="icon-remove"></i> ' + req.permission);
                } else {
                    cell.find('span').filter(function() {
                        return req.permission == $(this).text();
                    }).remove();

                    link.data("action", "addPermission")
                        .html('<i class="icon-plus"></i> ' + req.permission);
                }

            });
        });
    });
});



$(function() {
    /*
     * Second approach: find elements from DOM and add click listener.
     * 'data-xxx' attributes can be added to pass element specific data.
     */
   // $('form input #register-login').blur(function() {
	 $('#register form input[name=login]').blur(function() {
        //var input = $(this).find('input[name=login]');  // wrap DOM element in jQuery object
		 var input = $(this);
		// alert(input.val());
		 $('#register form').find('#register-login-error').text("");
        
                var req = {
                "login" : input.val(),
                "exists" : input.data("exists")
                };
                //alert(input.data("exists"));

            $.post(
            		baseUrl + '/validate', 
            		req,
            		function(data) {
            			//alert(data.exists);
                
		              if (data.exists == true) {          	
		            	  //var txt = 'Цей логін вже зайнято. Спробуйте інший.'; 
		            	  var txt = 'Choose another login!';
		            	  //alert(txt);
		            	  // input.append('<p class="">' + txt + '</p>');
		            	  //$('#register form p #register-login-error').text(txt);
		            	  $('#register-login-error').text(txt);
		            	  input.val("");
		               } 
		              
		            },
		            'json'
            );
            
            return false;
        
            
        });
    
    });
