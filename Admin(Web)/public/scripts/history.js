const db = firebase.firestore();
var db_history = db.collection('history');

//Check User
function checkUser()
{
    var user = firebase.auth().currentUser;
}

//Simple History Write
function writeHistory(user)
{

}