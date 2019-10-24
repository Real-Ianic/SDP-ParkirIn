const db = firebase.firestore();
var users = db.collection('user');

document.addEventListener('DOMContentLoaded', 
    function() 
    {
        load_users();
        M.AutoInit();
    }
);

function add_user()
{

    if($("#emailInput").hasClass('valid'))
    {
        var new_role = $("#roleInput").val();

        if(new_role != null)
        {
            var new_email = $("#emailInput").val();
            var new_pos = $("#posInput").val();

            if(new_role == "master")
            {
                new_pos = "-";
            }

            db.collection("user").add
            (
                {
                    email: new_email,
                    role: new_role,
                    pos: new_pos
                }
            )
            .then
            (
                function (e) 
                {
                    M.toast({html : `${new_email} Berhasil Ditambahkan Sebagai ${new_role} - ${new_pos}`, classes: 'rounded'});
                
                    // clear Inputs
                    $('#emailInput').val("");
                    $('#posInput').val("");

                }
            )
            .catch
            (
                function (err)
                {
                    M.toast({html : `An Error Occured, ${err}`, classes: 'rounded'});
                }
            )
        }
        else
        {
            M.toast({html : "Role Tidak Boleh Kosong !", classes: 'rounded'});
        }
    }
    else
    {
        M.toast({html : "Email Tidak Valid !", classes: 'rounded'});
    }
}

function load_users()
{
    var body_table_users = $("#body_table_users");

    users.onSnapshot
    (
        function(snapshot)
        {
            snapshot.docChanges().forEach
            (
                function(user_obj)
                {
                    var user = user_obj.doc;
                    
                    if(user_obj.type == 'added')
                    {
                        // Add Data To Table
                        body_table_users.append
                        (
                            `
                                <tr id='row_${user.id}'>
                                    <td>${user.data().email}</td>
                                    <td>${user.data().pos}</td>
                                    <td>${user.data().role}</td>
                                </tr>
                            `
                        )
                    }
                    else if(user_obj.type == 'modified')
                    {
                        $(`#row_${user.id}`).html
                        (
                            `
                                <td>${user.data().email}</td>
                                <td>${user.data().pos}</td>
                                <td>${user.data().role}</td>
                            `
                        );
                    }
                    else if(user_obj.type == 'removed')
                    {
                        // Remove Data From Table
                        $(`#row_${user.id}`).remove();
                    }
                }
            )
        }
    )
    ;
}