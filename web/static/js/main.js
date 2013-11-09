
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


/*
 * Second approach: find elements from DOM and add click listener.
 * 'data-xxx' attributes can be added to pass element specific data.
 */
//$(function() {
//    
//    $('#register form input[name=login]').blur(function() {
//        var input = $(this);
//
//        $('#register form').find('#register-login-error').text("");
//
//        $.post(
//            baseUrl + '/validate',
//            { "login" : input.val() },
//            function(data) {
//                if (data.exists) {
//                    var txt = 'Choose another login!';
//                    $('#register-login-error').text(txt);
//                    // don't clear input:  input.val("");
//                }
//
//            },
//            'json'
//        );
//
//        return false;
//
//
//    });
//
//});

$(function() {
    
    $('#register form input[name=login]').blur(function() {
        var input = $(this);

        $.post(
            baseUrl + '/validate',
            { "login" : input.val() },
            function(data) {
                if (data.exists) {
//                    var txt = 'Choose another login!';
//                    $('#register-login-error').text(txt);
                	$('#register-login-error').show();
                }
                else {
                	$('#register-login-error').hide();
                }

            },
            'json'
        );

        return false;


    });
    
    
    $('#register form input[name=password]').blur(function() {
        var input = $(this);

        $.post(
            baseUrl + '/validate',
            { "password" : input.val() },
            function(data) {
                if (!data.r_pass) {
                	$('#register-password-error').show();
                }
                else {
                	$('#register-password-error').hide();
                }

            },
            'json'
        );

        return false;


    });


});
