const db = firebase.firestore();
var db_teams = db.collection('teams');

var team1_ready = false;
var team2_ready = false;

var teamRes1 = {};
var teamRes2 = {};

// Trade Cart
var beforeTrade1 = new Map();
var beforeTrade2 = new Map();

// LOGGINGS
var tradeKiri = {};
var tradeKanan = {};

// TEAMS MAP
var team_map = new Map(); 

// Fetch Teams
document.addEventListener
(
    'DOMContentLoaded', 
    function()
    {
        $("#row_inputs").hide();
        $("#row_status").hide();

        loadTeams();        
        $("select").change
        (
            function()
            {
                // Reset Ready
                reset_ready();
            }
        );
    }
);

function loadItems()
{
    // Load Teams Item From team_map
    var team1 = team_map.get($("#comboBoxTeams1").val());
    var team2 = team_map.get($("#comboBoxTeams2").val());

    // Load Teams Resources / Items Whatever :v
    var team1_resources = team1.resource;
    var team2_resources = team2.resource;

    // Update Tampilan Nama
    $("#nama_team1").html($("#comboBoxTeams1").val());
    $("#nama_team2").html($("#comboBoxTeams2").val());

    // Clear Both Table
    $("#listResource1").html('');
    $("#listResource2").html('');

    // Clear Both Combobox
    $("#comboBoxResource1").empty();
    $("#comboBoxResource2").empty();

    // Insert Items To Table Team 1
    for (resource in team1_resources)
    {
        // Append Data To Table
        $("#listResource1").append
        (
            `
                <tr>
                    <td>${resource}</td>
                    <td>${team1_resources[resource]}</td>
                </tr>
            `
        );

        // Append Data To ComboBox
        $("#comboBoxResource1").append
        (
            `
                <option value='${resource}'>${resource}</option>
            `
        );
    }
    
    // Insert Items To Table Team 2
    for (resource in team2_resources)
    {
        // Append Data To Table
        $("#listResource2").append
        (
            `
                <tr>
                    <td>${resource}</td>
                    <td>${team2_resources[resource]}</td>
                </tr>
            `
        );

        // Append Data To ComboBox
        $("#comboBoxResource2").append
        (
            `
                <option value='${resource}'>${resource}</option>
            `
        );
    }

    M.AutoInit();

}

function updateDataLocal()
{
    // Update Map Locally
    // Load Teams Item From team_map
    var team1 = team_map.get($("#comboBoxTeams1").val());
    var team2 = team_map.get($("#comboBoxTeams2").val());

    // Load Teams Resources / Items Whatever :v
    var team1_resources = team1.resource;
    var team2_resources = team2.resource;

    // Add And Remove Items At BeforeTrade1
    beforeTrade1.forEach
    (
        (val, key) =>
        {
            // Add Resources To Other Team
            if(team2_resources[key] != null)
            {
                // If Data Exist
                team2_resources[key] += val * 1;
            }
            else
            {
                // If Data Does Not Exist
                team2_resources[key] = val * 1;
            }

            // Remove Resources From Team1
            team1_resources[key] -= val;

            // LOG
            tradeKiri[key] = val;
        }
    )

    beforeTrade2.forEach
    (
        (val, key) =>
        {
            // Add Resources To Other Team
            if(team1_resources[key] != null)
            {
                // If Data Exist
                team1_resources[key] += val * 1;
            }
            else
            {
                // If Data Does Not Exist
                team1_resources[key] = val * 1;
            }

            // Remove Resources From Team2
            team2_resources[key] -= val;

            // LOG
            tradeKanan[key] = val;
        }
    )

    updateDatabase();
}

function updateDatabase()
{
    // Load Teams Item From team_map
    var teamName1 = $("#comboBoxTeams1").val();
    var teamName2 = $("#comboBoxTeams2").val();

    var team1 = team_map.get($("#comboBoxTeams1").val());
    var team2 = team_map.get($("#comboBoxTeams2").val());

    db_teams.doc(teamName1).update
    (
        {
            resource: team1.resource
        }
    )
    .then
    (
        () =>
        {
            M.toast({html: "Trade Sukses 1", classes: 'rounded'});
        }
    )
    .catch
    (
        (error) =>
        {
            console.error("Error Writing Document: ", error);
        }
    );

    db_teams.doc(teamName2).update
    (
        {
            resource: team2.resource
        }
    )
    .then
    (
        () =>
        {
            M.toast({html: "Trade Sukses 2", classes: 'rounded'});

            // WRITE LOGS
            write_log(`Trade Team ${teamName1} dan Trade Team ${teamName2}`, {tradeKiri, tradeKanan});

            // RESET PAGE AFTER TRADE
            reset_page();
        }
    )
    .catch
    (
        (error) =>
        {
            console.error("Error Writing Document: ", error);
        }
    );
}

function tradeButton()
{
    var validTrade = true;

    // Get Both Teams
    var team1 = team_map.get($("#comboBoxTeams1").val());
    var team2 = team_map.get($("#comboBoxTeams2").val());

    // VALIDASI TRADE
    beforeTrade1.forEach
    (
        (value,key,map) =>
        {
            if(team1.resource[key] - value < 0)
            {
                validTrade = false;
            }
        }
    );
    
    beforeTrade2.forEach(
        (value,key,map) =>
        {
            if(team2.resource[key] - value < 0)
            {
                validTrade = false;
            }
        }
    );

    if(validTrade)
    {
        updateDataLocal();
    }
    else
    {
        M.toast({html: `Invalid Trade ! Resource anda kurang !`, classes: 'rounded'});
    }
}

function additem(caller)
    {
        if(caller == "additem1")
        {
            var jenisItem = $("#comboBoxResource1").val();
            var jumlahItem = $("#numericResource1").val();

            $("#collection1").append("<li class='collection-item'>"+jenisItem+"|"+jumlahItem+"X</li>");
            
            beforeTrade1.set(jenisItem,jumlahItem);
        }
        else if(caller == "additem2")
        {
            var jenisItem = $("#comboBoxResource2").val();
            var jumlahItem = $("#numericResource2").val();

            $("#collection2").append("<li class='collection-item'>"+jenisItem+"|"+jumlahItem+"X</li>");
            
            beforeTrade2.set(jenisItem,jumlahItem);
        }
    }

function allReadyEvent()
{
    var teamname1 = $("#comboBoxTeams1").val();
    var teamname2 = $("#comboBoxTeams2").val();

    if(team1_ready && team2_ready)
    {
        //Semua sudah siap
        if(teamname1 == teamname2)
        {
            //Team Sama, reset Team
            reset_ready();
            M.toast({html: `Team Tidak boleh Sama !`, classes: 'rounded'});
        }
        else
        {
            $("#selectTeam1").hide();
            $("#selectTeam2").hide();

            $("#row_inputs").show();
            $("#row_status").show();
            $("#row_cart").show();
            $("#row_buttons").show();

            loadItems();
        }
    }
}

function reset_page()
{
    reset_ready();
    beforeTrade1 = new Map();
    beforeTrade2 = new Map();

    tradeKiri = {};
    tradeKanan = {};

    $("#collection1").html('');
    $("#collection2").html('');

    $("#listResource1").html('');
    $("#listResource2").html('');

    $("#comboBoxResource1").html('');
    $("#comboBoxResource2").html('');

    $("#selectTeam1").show();
    $("#selectTeam2").show();

    $("#row_inputs").hide();
    $("#row_status").hide();
    $("#row_buttons").hide();
    $("#row_cart").hide();

    $("#numericResource1").val(0);
    $("#numericResource2").val(0);
}

function reset_ready()
{
    // Reset Ready State
    team1_ready = false;
    team2_ready = false;

    // Change Button
    $("#button_ok_kiri").html("Ready");
    $("#button_ok_kanan").html("Ready");
}

function button_ready(caller)
{
    if(caller == "button_ok_kiri")
    {
        team1_ready = !team1_ready;
    }
    else if(caller == "button_ok_kanan")
    {
        team2_ready = !team2_ready;
    }
    else
    {
        M.toast({html: `Error, Unknown Caller`, classes: 'rounded'});
    }
    update_button_ok();
    allReadyEvent();
}

function update_button_ok()
{
    if(team1_ready)
    {
        $("#button_ok_kiri").html("Unready");
    }
    else
    {
        $("#button_ok_kiri").html("Ready");
    }

    if(team2_ready)
    {
        $("#button_ok_kanan").html("Unready");
    }
    else
    {
        $("#button_ok_kanan").html("Ready");
    }
}

function loadTeams()
{
    var comboGroupTeams = $('.groupTeams');
    var comboGroupAdmin = $('.groupAdmin');

    // Load Teams Menggunakan OnSnapshot
    db_teams.onSnapshot
    (
        (snapshot) =>
        {
            snapshot.docChanges().forEach
            (
                (team_obj) =>
                {
                    var team = team_obj.doc;

                    if(team_obj.type == 'added')
                    {
                        // Add To Combobox
                        if(team.id != 'panitia')
                        {
                            comboGroupTeams.append(`<option value=${team.id}>${team.id}</option>`);
                        }
                        else
                        {
                            comboGroupAdmin.append(`<option value=${team.id}>${team.id}</option>`);
                        }

                        // Add Teams To Map
                        team_map.set(team.id, team.data());
                        
                    }
                    else if(team_obj.type == 'modified')
                    {
                        // Something's modified at team collection... (Money, Items, Resource, etc)
                        team_map.set(team.id, team.data());

                        loadItems();

                    }
                    else if(team_obj.type == 'removed')
                    {
                        console.log(`Something's Removed!`);
                    }
                }
            )

            // After Load Init Materialize
            $(".loadingTeams").remove();
            M.AutoInit();

        }
    );
}