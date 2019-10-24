// Setup Database
var db = firebase.firestore();

function signIn()
{
    var provider = new firebase.auth.GoogleAuthProvider();
    firebase.auth().signInWithPopup(provider);
}

function signOut()
{
    firebase.auth().signOut();
}

function check_user(user)
{
    db.collection('user')
    .where('email', '==', user.email )
    .get()
    .then
    (
        (query_snapshot) =>
        {
            if(query_snapshot._snapshot.docChanges.length > 0)
            {
                query_snapshot.forEach
                (
                    (user_in_db) =>
                    {
                        if(user_in_db.data().role == 'master')
                        {
                            // location.replace("master-mainpage.html");
                            $('#row_navs').show();
                            $('#btn_master').show();
                            $('#btn_pos').show();
                            $('#btn_audit').show();
                        }
                        else if(user_in_db.data().role == 'penjaga pos minigame')
                        {
                            // location.replace("penjagapos-mainpage.html");
                            $('#row_navs').show();
                            $('#btn_master').hide();
                            $('#btn_pos').show();
                            $('#btn_audit').hide();
                        }
                        else if(user_in_db.data().role == "penjaga auditorium")
                        {
                            // location.replace("penjagaaudit-mainpage.html");
                            $('#row_navs').show();
                            $('#btn_master').hide();
                            $('#btn_pos').hide();
                            $('#btn_audit').show();
                        }
                    }
                )
            }
            else
            {
                firebase.auth()
                .signOut()
                .then
                (
                    () => 
                    {
                        window.location = "https://www.youtube.com/watch?v=oGJr5N2lgsQ";
                    }
                );
            }
        }
    ); 
}

firebase.auth().onAuthStateChanged
(
    (user) => 
    {
        // Check User Null?
        if(user != null)
        {
            $('#sign-in').attr('style', 'display:none');
            $('#sign-out').attr('style', 'display:block');
            check_user(user);
        }
        else
        {
            $('#row_navs').hide();
            $('#btn_master').hide();
            $('#btn_pos').hide();
            $('#btn_audit').hide();
            M.toast({html:'Signed out', classes:'rounded'});
            console.log("Signed out");
            $('#sign-out').attr('style', 'display:none');
            $('#sign-in').attr('style', 'display:block');
        }
    }
);