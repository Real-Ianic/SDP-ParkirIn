$("#sign-in").click(
  function(){
    var provider = new firebase.auth.GoogleAuthProvider();
    firebase.auth().signInWithPopup(provider);
  }
);

$("#sign-out").click(
  function(){
    firebase.auth().signOut();
    $('#load').html("");
  }
);

var db;
var currUser;
$(document).ready(
  function () {
      M.AutoInit();
      currUser = null;
      db = firebase.firestore();
      //firebase.auth().signOut();
  }
);

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    $('#sign-in').attr('style', 'display:none');
    $('#sign-out').attr('style', 'display:block');
    let u = firebase.auth().currentUser;
    //console.log(u != null);
    if (u != null){
      $("#load").append("<br>DISPLAY NAME: "+u.displayName);
      $("#load").append("<br>E-mail: "+u.email)
    }
    else {
      console.log('u is null');
    }
    // Ngecek Purposes only
    // console.log(u.displayName);
    // console.log(user.uid);
    // console.log(user.email);

    var userLogging = user.email;
    var foundUser = 0;
    let ref = db.collection("user").get()
    .then(
        function (snapshot) {
            var role = 'not found';
            snapshot.forEach(
                function (doc) {
                    //console.log(doc.data().email + " HALO ");
                    var temp = doc.data().email;
                    if(temp == userLogging)
                    {
                      foundUser = 1;
                      role = doc.data().role;
                      currUser = doc.data();
                    }
                }
            )

            if(foundUser == 1){
              //CEK ROLE DISINI
              console.log(role);
              console.log(currUser);
              //saran : jangan di redirect biar bisa logout (?)
              //atau kasih tombol logout di mainpage nya tiap role

              // if(role == "master"){
              //   location.replace("master-mainpage.html");
              // }
              // else if(role == "penjaga pos minigame"){
              //   location.replace("penjagapos-mainpage.html");
              // }
              // else if(role == "penjaga auditorium"){
              //   location.replace("penjagaaudit-mainpage.html")
              // }
              
            }
            else{
              firebase.auth().signOut();
              alert('Tidak terdaftar');
              window.location.assign('https://www.w3schools.com');
            }
        }
    )
    .catch(
        function (err) {
            console.error(err);
        }
    );
  } 
  else {
    // No user is signed in.
    console.log('no user here');
    $('#sign-out').attr('style', 'display:none');
    $('#sign-in').attr('style', 'display:block');
  }
});