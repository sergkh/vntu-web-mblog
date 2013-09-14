
/*
 * First approach: Function called from HTML by adding onclick attribute.
 */
function manageUser(userId, action) {

    var request = {
        "userId" : userId,
        "action" : action
    }

    $.post(
        baseUrl + '/admin',
        request,
        function() {
            alert('callback')
        },
        'json'
    );


   return false;
}

$(function() {
    /*
     * Second approach: find elements from DOM and add click listener.
     * 'data-xxx' attributes can be added to pass element specific data.
     */
    $('#users-table tr').each(function() {
        var row = $(this);

        row.on('click', 'a[data-action]', function(evt){
            var link = $(this);

            var req = {
                "userId" : row.data("user"),
                "action" : link.data("action"),
                "permission" : link.data("permission")
            }

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

            })
        });
    });
});