// Setup Database
const db = firebase.firestore();
var teams = db.collection('teams');
var products = db.collection('product');

var team_map = new Map();
var product_map = new Map();
var barang_dibeli = [];

document.addEventListener('DOMContentLoaded',
    function()
    {
        loadTeams
        (
            function()
            {
                M.AutoInit();
                //btnSelectTeam();
            }
        );
        loadProducts();
        update_estimation();
    }
);

function loadTeams(callback)
{

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
            callback();
        }
    );
}

function loadProducts()
{
    products.onSnapshot
    (
        function (snapshot)
        {
            snapshot.docChanges().forEach
            (
                function (product_obj)
                {
                    var product = product_obj.doc;

                    if(product_obj.type == 'added')
                    {
                        // Product Is Created / Newly read
                        product_map.set(product.id, product.data());

                        var body_table_item = $("#bodyTableProduct");

                        body_table_item.append
                        (
                            `
                                <tr id='row_${product.id}'>
                                    <td><img src='images/${product.data().nama.toLowerCase()}.png' style='width:50px;'></td>
                                    <td id='nama_${product.id}'>${product.data().nama}</td>
                                    <td id='harga_${product.id}'>${product.data().harga * product.data().mult}</td>
                                    <td class='owned' id='owned_${product.id}'>0</td>
                                    <td>
                                        <div class='input-field row'>
                                            <input type='number' name='quantity_${product.id}' min='0' onchange='update_subtotal("${product.id}")' id='quantity_${product.id}' class='col s6'>
                                            <label for='quantity_${product.id}'>Qty</label>
                                        </div>
                                    </td>
                                    <td id='subtotal_${product.id}'>0</td>
                                </tr>
                            `
                        );
                    }
                    else if(product_obj.type == 'modified')
                    {
                        // Product Is Modified
                        product_map.set(product.id, product.data());
                        $(`#harga_${product.id}`).html(product.data().harga * product.data().mult);
                        update_subtotal(product.id);
                    }
                    else if(product_obj.type == 'removed')
                    {
                        // Product is Removed
                        M.toast({html: `Something Removed on Products, Please Refresh`, classes: 'rounded'});
                    }
                }
            )
        }
    );
}

function btnSelectTeam()
{
    // Team is selected, update Owned
    var teamName = M.FormSelect.getInstance($('#comboBoxTeams')).input.value;
    var team = team_map.get(teamName);

    // Update Money
    $(`#namaTeam`).html(`${teamName}`);
    $("#textMoney").html(`'s Money: ${team.money}`);

    var error = false;
    product_map.forEach
    (
        function(product, key)
        {
            if(team.resource != null)
            {
                if(team.resource[product.nama] != null)
                {
                    $(`#owned_${key}`).html(team.resource[product.nama]);
                    $(`#quantity_${key}`).attr({max: team.resource[product.nama], min: 0, value: 0});
                    document.querySelector(`#row_${key}`).removeAttribute('style');
                }
                else
                {
                    $(`#owned_${key}`).html(0);
                    document.querySelector(`#row_${key}`).setAttribute('style', 'display:none');
                    $(`#quantity_${key}`).attr({max: 0, min: 0, value: 0});
                }
            }
            else
            {
                error = true;
            }
        }
    )

    if(error)
    {
        ShowSelectTeams();
        M.toast({html: `Woops, Something Went Wrong :(`, classes: 'rounded'});
    }

    M.updateTextFields();
}

function update_subtotal(product_id)
{
    let nama = $(`#nama_${product_id}`).html();
    let jumlah = Number($(`#quantity_${product_id}`).val());
    let hargasatuan = Number($(`#harga_${product_id}`).html());
    $(`#subtotal_${product_id}`).html(hargasatuan * jumlah);
    let subtotal = $(`#subtotal_${product_id}`).html();
    barang_dibeli[nama] = [hargasatuan, jumlah, subtotal];
}

function ShowSelectTeams()
{
    $("#cardSelectTeam").show();
    $("#cardSellItem").hide();
    $(":input[type='number']").val(0);
}

function ShowSellCard()
{
    $("#cardSelectTeam").hide();
    $("#cardSellItem").show();
    $(":input[type='number']").val(0);
    barang_dibeli = [];
}

function fill()
{
    $('#tabelResource').html('');
    var isiHTML = "";    
    var totalPendapatan = 0;

    for (const key in barang_dibeli) 
    {
        if (barang_dibeli.hasOwnProperty(key)) {
            const element = barang_dibeli[key];
            if (Number(element[1]) > 0){
                isiHTML += `<tr>
                                <td>${key}</td>
                                <td>${element[0]}</td>
                                <td>${element[1]}</td>
                                <td>${element[2]}</td>
                            </tr>`;
                totalPendapatan += Number(element[2]);
            }
        }
    }

    $('#tabelResource').html(isiHTML);
    $(`#total`).html(totalPendapatan);
}

function finalize()
{
    let namaTeam = $(`#namaTeam`).html();
    const teamRef = db.collection(`teams`).doc(namaTeam);
    updatean = {};
    
    var item_log = {};

    for (const key in barang_dibeli) 
    {
        if (barang_dibeli.hasOwnProperty(key)) 
        {
            const element = barang_dibeli[key];
            updatean[`resource.${key}`] = firebase.firestore.FieldValue.increment(-1*Number(element[1]));
            item_log[key] = {harga: element[0], jumlah: element[1], subtotal: element[2]};
        }
    }

    let totalpoin = $(`#total`).html();
    updatean['poin'] = firebase.firestore.FieldValue.increment(Number(totalpoin));

    teamRef.update(updatean).then
    (
        ()=>
        {
            write_log(`Team ${namaTeam} menjual barang dan Gain ${totalpoin} points`, {items: item_log, total_points: total});
            M.toast({html:`Sale successful. Gain ${totalpoin} points`, classes: 'rounded'});
            ShowSelectTeams();
        }
    );
}