// Setup Database
const db = firebase.firestore();
var teams = db.collection('teams');
var products = db.collection('product');

// Setup resoruce
var team_map = new Map();
var recipe_map = new Map();

document.addEventListener('DOMContentLoaded', function(){
    loadTeams
    (
        function()
        {
            $("#loadingTeams").remove();
            M.AutoInit();
        }
    );
    loadItems();
});

function loadTeams(callback)
{
    console.log("loading teams");
    var comboGroupTeams = $('#groupTeams');
    var comboGroupAdmin = $('#groupAdmin');

    teams.onSnapshot
    (
        function(snapshot)
        {
            snapshot.docChanges().forEach
            (
                function(team_obj)
                {
                    var team = team_obj.doc;

                    if(team_obj.type == 'added')
                    {
                        if(team.id != 'panitia')
                        {
                            comboGroupTeams.append(`<option value=${team.id}>${team.id}</option>`);
                        }
                        else
                        {
                            comboGroupAdmin.append(`<option value=${team.id}>${team.id}</option>`);
                        }
                        team_map.set(team.id, team.data());
                    }
                    else if(team_obj.type == 'modified')
                    {
                        // Something modified (Resource / Item / Team)
                        team_map.set(team.id, team.data());
                    }
                    else if(team_obj.type == 'removed')
                    {
                        M.toast({html: `Something Removed on Teams, Please Refresh`, classes: 'rounded'});
                    }
                }
            )
            loadTeamResource();
        }
    );
    callback();
}

function loadTeamResource()
{
    var teamName = M.FormSelect.getInstance($('#comboBoxTeams')).input.value;
    console.log(`Loading ${teamName}'s Resources`);

    var team_resource = team_map.get(teamName).resource;
    console.log(team_resource);
    if(team_resource != null)
    {
        
    }
    else
    {
        team_resource = {};
        // ShowSelectTeams();
        // M.toast({html: `Woops, Something Went Wrong :(`, classes: 'rounded'});
    }

    for(var key in team_resource)
    {
        // Load Resource to class resource_{nama_resource}
        if(team_resource[key] == undefined)
        {
            $(`.resource_${key}`).html('0');
        }
        else
        {
            $(`.resource_${key}`).html(team_resource[key]);
        }
    }
}

function klik(elem){
    elem.click();
}

function loadItems()
{
    console.log("loading craftable items");

    var tabBodyItems = $('#tabBodyItems');

    products.get().then
    (
        function(snapshot)
        {
            snapshot.forEach
            (
                function(item)
                {
                    // Create New Row
                    tabBodyItems.append(`<tr id='${item.id}'></tr>`);

                    // Insert Columns For New Row
                    var newTr = $(`#${item.id}`);
                    newTr.append(`<td><img src='images/${item.data().nama.toLowerCase()}.png' style='width:50px;'></td><td>${item.data().nama}</td>`);
                    newTr.append(`<td colspan=3><div id='resep_${item.id}'></ul></td>`);
                    newTr.append
                    (
                        `
                        <td>
                            <div class='input-field'>
                                <input class='craftingPurposes' resep='${item.data().nama}' id='numeric_${item.data().nama}' type='number' min='0' value=0></input>
                                <label for='numeric_${item.data().nama}'>Amount</label>
                            </div>
                        </td>
                        `
                    );

                    var tempMap = new Map();
                    var dropdownResep = $(`#resep_${item.id}`);
                    dropdownResep.append(
                        `<div>
                            <table class='table'>
                                <tbody id='tbody_recipe_${item.id}'>
                                </tbody>
                            </table>
                        </div>
                        `
                    )

                    /*
                    // Menyimpan Recipe Dalam Map
                    var tempMap = new Map();
                    

                    // Create New Recipe Modal
                    var modal_collections = $("#modal_collections");
                    modal_collections.append
                    (
                        `
                        <div class="modal modal-fixed-footer" id="modal_recipe_${item.id}">
                            <div class="modal-content">
                                <h4>Recipe For ${item.data().nama}</h4>
                                <div class='card hoverable'>
                                    <div class='card-content'>
                                        <table class='table bordered striped responsive-table highlight'>
                                            <thead>
                                                <tr>
                                                    <th>Resource</th>
                                                    <th>Amount</th>
                                                    <th>You Have</th>
                                                </tr>
                                            </thead>
                                            <tbody id='tbody_recipe_${item.id}'>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <!-- FOOTER MODAL -->
                            <div class="modal-footer">
                                <button class="modal-close btn-flat">Close</button>
                            </div>
                        </div>
                        `
                    );
                    */
                    // Get Recipe Table
                    var newTbody = $(`#tbody_recipe_${item.id}`);

                    // ForEach Resource in Recipe, Create New Row 
                    var bahan = item.data().bahan;
                    for(var key in bahan)
                    {
                        tempMap.set(key,bahan[key]);
                        newTbody.append
                        (
                            `
                            <tr>
                                <td>${key}</td>
                                <td>${bahan[key]}</td>
                                <td class='resource_${key}'></td>
                            </tr>
                            `
                        )
                    }

                    // Masukkan Resep Ke Map
                    recipe_map.set(item.data().nama,tempMap);
                    
                }
            )
        }
    )
    .catch
    (
        function(error)
        {
            M.toast({html: `Error, ${error}`, classes: 'rounded'});
        }
    )
    .finally
    (
        function()
        {
            M.updateTextFields();
            M.AutoInit();
        }
    )
}

function update_estimation()
{
    var multiplier = $("#smeltValue").val() / 10;
    var est_gold = multiplier * 4;
    var est_iron = multiplier * 6;

    $("#p_estimation").html(`Got ${est_gold} Gold, and ${est_iron} Iron`);
}


$('#btnCraft').click(
    function(){
        
        var teamMap = team_map.get(M.FormSelect.getInstance($('#comboBoxTeams')).input.value);
        var teamResource = teamMap.resource;

        var validCraft = true;
        var mauDiCraft = $(`.craftingPurposes`);
        var updatean = {};
        var products = {};
        for (let index = 0; index < mauDiCraft.length; index++) {
            const element = mauDiCraft[index];
            let banyak = Number(element.value)
            //console.log(element);
            var namaProduct = element.getAttribute('resep');
            if (banyak>0){
                let resep = recipe_map.get(namaProduct);
                var terpakai = {};
                
                resep.forEach((val, key)=>{
                    let butuh = banyak * val;
                    if (teamResource[key] < butuh){
                        validCraft = false;
                    } else {
                        let strKey = 'resource';
                        strKey += `.${key}`
                        if (typeof updatean[strKey] !== 'undefined'){
                            updatean[strKey]._operand += -(butuh)
                        } else {
                            updatean[strKey] = firebase.firestore.FieldValue.increment(-(butuh));
                        }
                        terpakai[key] = butuh;
                    }
                });

                if (validCraft){
                    let strKey = 'resource';
                    strKey += `.${namaProduct}`
                    if (typeof updatean[strKey] !== 'undefined'){
                        updatean[strKey]._operand += banyak
                    } else {
                        updatean[strKey] = firebase.firestore.FieldValue.increment(banyak);
                    }
                    products[namaProduct] = {'jumlah':banyak, 'bahan':terpakai};
                } else {
                    break;
                }
            }
        }
        if(!validCraft) {
            M.toast({html: 'Craft tidak valid ! Resource kurang', classes: 'rounded'});
        }
        else
        {
            let namaTim = M.FormSelect.getInstance($('#comboBoxTeams')).input.value;
            console.log(namaTim);
            console.log(updatean);
            teams.doc(namaTim).update(updatean).then(()=>{
                write_log(`Team ${namaTim} Crafting`, {products});
                M.toast({html: 'Craft Sukses !', classes: 'rounded'});
                ShowSelectTeams();
            }).catch((err)=>{
                M.toast({html: err, classes: 'rounded'});
            }).finally(
                ()=>{ShowSelectTeams();}
            )
        }

    }
)

$('#btnSmelt').click(
    function(){

        var numericInput = $("#smeltValue").val();

        if(numericInput % 10 != 0 || numericInput == 0)
        {
            //Bukan Kelipatan Sepuluh
            M.toast({html: 'Invalid Input ! Input harus kelipatan sepuluh', classes: 'rounded'});
        }
        else
        {
            var team_resources = team_map.get($("#comboBoxTeams").val()).resource;

            // Check Punya Furnace ?
            if(team_resources.Furnace > 0)
            {
                // Punya Furnace
                // Check Ore Cukup?
                if(team_resources.Ore > 0)
                {
                    // Kurangi Resource Dari Team Dan Hitung Pendapatan
                    var multiplier = $("#smeltValue").val() / 10;
                    
                    // Kurangi Ore Dari Team
                    team_resources.Ore -= $("#smeltValue").val();
                    
                    // Tambahkan Gold Dan Iron
                    // Check Gold dan Iron Kosong?
                    if(team_resources.Gold == undefined)
                    {
                        team_resources.Gold = 0;
                    }

                    if(team_resources.Iron == undefined)
                    {
                        team_resources.Iron = 0;
                    }

                    // Tambahkan Resource!
                    team_resources.Gold += (multiplier * 4);
                    team_resources.Iron += (multiplier * 6);

                    // Update Database
                    teams.doc($("#comboBoxTeams").val())
                    .update
                    (
                        {
                            resource: team_resources
                        }
                    )
                    .then
                    (
                        () =>
                        {
                            write_log(`Smelting Ore Team ${$("#comboBoxTeams").val()}`, {Gold: (multiplier * 4), Iron: (multiplier * 6)});
                            M.toast({html: `Berhasil Smelt ! Mendapatkan ${(multiplier * 4)} Gold Dan ${(multiplier * 6)} Iron !`, classes: 'rounded'});
                            ShowSelectTeams()
                        }
                    )
                    .catch
                    (
                        (error) =>
                        {
                            M.toast({html: `Gagal Smelting ! ${error}`, classes: 'rounded'});
                        }
                    )
                    ;
                }
                else
                {
                    M.toast({html: 'Invalid Input ! Input melebihi yang dimiliki', classes: 'rounded'});
                }
            }
            else
            {
                M.toast({html: 'Tidak Memiliki Furnace !', classes: 'rounded'});
            }
                
        }
    }
)