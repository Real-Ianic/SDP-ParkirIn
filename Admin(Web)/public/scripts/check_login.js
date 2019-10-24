// var db = firebase.firestore();
var logged_in_user;

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
                        logged_in_user = user_in_db.data();
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
                        location.replace("index.html");
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
            check_user(user);
        }
        else
        {
            location.replace("index.html");
        }
    }
);

function write_log(message, args = '')
{
    if(args == '')
    {
        db.collection('logs').add
        (
            {
                displayName: firebase.auth().currentUser.displayName,
                email: logged_in_user.email,
                message: message,
                pos: logged_in_user.pos,
                time: firebase.firestore.FieldValue.serverTimestamp()
            }
        );
    }
    else
    {
        db.collection('logs').add
        (
            {
                data: args,
                displayName: firebase.auth().currentUser.displayName,
                email: logged_in_user.email,
                message: message,
                pos: logged_in_user.pos,
                time: firebase.firestore.FieldValue.serverTimestamp()
            }
        );
    }    
}