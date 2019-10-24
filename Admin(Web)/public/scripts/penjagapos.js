// Setup Database
const db = firebase.firestore();
var teams = db.collection('teams');
var team_names = [];


document.addEventListener('DOMContentLoaded', function(){
    loadTeams
    (
        function()
        {
            M.AutoInit();
        }
    );
});

function loadTeams(callback)
{
    // Loading Teams Not Using Snapshot (One Time Read Only)
    teams.get().then
    (
        function (snapshot)
        {
            snapshot.forEach
            (
                function(team)
                {
                    if(team.id != 'panitia')
                    {
                        $(".groupTeams").append(`<option value=${team.id}>${team.id}</option>`);
                    }
                    else
                    {
                        $(".groupAdmin").append(`<option value=${team.id}>${team.id}</option>`);
                    }
                    team_names.push(team.id);
                }
            )
            callback();
        }
    )
}

function show_confirm_solo()
{
    var reward = $("#comboBoxPrizeSolo").val();
    var team = $("#comboBoxTeamsSolo").val();
    // Change Paragraf in Solo Modal
    $("#text_solo").html(`Apakah Anda Yakin Memberikan <b>${reward}</b> Reward Kepada Team <b>${team}</b>?`);
}

function show_confirm_versus()
{
    // Change Paragraf in Versus Modal
    var team_kiri = $("#comboBoxTeamsVersusKiri").val();
    var team_kanan = $("#comboBoxTeamsVersusKanan").val();
    var select_win = $("#selectWin").val();

    // Check Sama ?
    if(team_kiri != team_kanan)
    {
        if(select_win == 'team1')
        {
            // Team Kiri Menang
            $("#text_versus").html(`Apakah Anda Yakin Team <b>${team_kiri} Menang </b>Melawan Team <b>${team_kanan}</b>?`);
        }
        else if(select_win == 'team2')
        {
            // Team Kanan Menang
            $("#text_versus").html(`Apakah Anda Yakin Team <b>${team_kanan} Menang </b>Melawan Team <b>${team_kiri}</b>?`);
        }
        else if(select_win == 'draw')
        {
            $("#text_versus").html(`Apakah Anda Yakin Team <b>${team_kiri}</b> Dan Team <b>${team_kanan} Draw</b>?`);
        }
        else
        {
            M.toast({html: `Woops, Something went wrong :(`, classes: 'rounded'});
        }
        M.Modal.getInstance($("#modal_confirm_versus")).open();
    }
    else
    {
        M.toast({html: `Team Kanan Dan Kiri Tidak Boleh Sama !`, classes: 'rounded'});
    }
}

function confirm_solo()
{
    // Solo Confirmed
    var reward = $("#comboBoxPrizeSolo").val();
    var team = $("#comboBoxTeamsSolo").val();
    var duid_increment = 0;

    // GANTI REWARD DISINI \\
    if(reward == "Gold")
    {
        duid_increment = 50;
    }
    else if(reward == "Silver")
    {
        duid_increment = 25;
    }
    else if(reward == "Bronze")
    {
        duid_increment = 10;
    }

    // Update Team's Money
    db.collection('teams').doc(`${team}`).update
    (
        {
            money: firebase.firestore.FieldValue.increment(duid_increment)
        }
    )
    .then
    (
        function()
        {
            M.toast({html: `Pemberian Reward ${reward} Kepada Team ${team} Success !`, classes: 'rounded'});
        }
    )
    .catch
    (
        function(error)
        {
            M.toast({html: `Pemberian Reward ${reward} Kepada Team ${team} Error !, ${error}`, classes: 'rounded'})
        }
    );

    write_log(`Memberi Reward Sebesar ${duid_increment} Kepada Team ${team}`);
}

function confirm_versus()
{
    var team_kiri = $("#comboBoxTeamsVersusKiri").val();
    var team_kanan = $("#comboBoxTeamsVersusKanan").val();
    var select_win = $("#selectWin").val();

    var duid_increment_kiri = 0;
    var duid_increment_kanan = 0;

    if(select_win == 'team1')
    {
        // Team Kiri Menang
        duid_increment_kiri = 50;
        duid_increment_kanan = 20;
    }
    else if(select_win == 'team2')
    {
        // Team Kanan Menang
        duid_increment_kiri = 20;
        duid_increment_kanan = 50;
    }
    else if(select_win == 'draw')
    {
        // Draw
        duid_increment_kiri = 20;
        duid_increment_kanan = 20;
    }

    // Update Team Kiri's Money
    db.collection('teams').doc(`${team_kiri}`).update
    (
        {
            money: firebase.firestore.FieldValue.increment(duid_increment_kiri)
        }
    )
    .then
    (
        function()
        {
            M.toast({html: `Pemberian Reward Sebesar ${duid_increment_kiri} Kepada Team ${team_kiri} Success !`, classes: 'rounded'})
        }
    )
    .catch
    (
        function(error)
        {
            M.toast({html: `Pemberian Reward Sebesar ${duid_increment_kiri} Kepada Team ${team_kiri} Error !, ${error}`, classes: 'rounded'})
        }
    );

    // Update Team Kanan's Money
    db.collection('teams').doc(`${team_kanan}`).update
    (
        {
            money: firebase.firestore.FieldValue.increment(duid_increment_kanan)
        }
    )
    .then
    (
        function()
        {
            M.toast({html: `Pemberian Reward Sebesar ${duid_increment_kanan} Kepada Team ${team_kanan} Success !`, classes: 'rounded'})
        }
    )
    .catch
    (
        function(error)
        {
            M.toast({html: `Pemberian Reward Sebesar ${duid_increment_kanan} Kepada Team ${team_kanan} Error !, ${error}`, classes: 'rounded'})
        }
    );

    write_log(`Memberi Reward Sebesar ${duid_increment_kiri} Kepada Team ${team_kiri}, dan ${duid_increment_kanan} Kepada Team ${team_kanan}`);

}

function show_card()
{
    // Check Input
    var mode = $("#selectMode").val();

    if(mode == 'Solo')
    {
        // Show Solo Card
        $("#card_solo").show();
        $("#cardSelectMode").hide();
    }
    else if(mode == 'Versus')
    {
        // Show Versus Card
        $("#card_versus").show();
        $("#cardSelectMode").hide();
    }
}