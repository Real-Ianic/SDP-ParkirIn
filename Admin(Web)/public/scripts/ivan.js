$("#sign-in").click(
  function(){
    var provider = new firebase.auth.GoogleAuthProvider();
    firebase.auth().signInWithPopup(provider);
  }
);

$("#sign-out").click(
  function(){
    firebase.auth().signOut();
  }
);

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
    $("#sign-out").removeAttr("hidden");
    $("#sign-in").attr("hidden", true);
    let u = firebase.auth().currentUser;
    $("#load").append("<br>DISPLAY NAME: "+u.displayName)
    console.log(u.displayName)
  } else {
    // No user is signed in.
    $("#sign-in").removeAttr("hidden");
    $("#sign-out").attr("hidden", true);
  }
});