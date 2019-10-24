//document.ready tapi javascript
// Setup Database
const db = firebase.firestore();
var resourceRef = firebase.firestore().collection('resource');
var teamDocument = firebase.firestore().collection('teams');
var db_teams = firebase.firestore().collection('teams');

// MAPS
var team_map = {};
var item_map = {};

var temp = "";

document.addEventListener
('DOMContentLoaded', 
    function()
    {
        loadTeams();
        loadItems();
    }
);

function loadItems()
{
    resourceRef.onSnapshot
    (
        (qSnapshot)=>
        {
            qSnapshot.docChanges().forEach
            (
                (docChange)=>
                {
                    var item = docChange.doc;
                    
                    if(docChange.type == 'added' || docChange.type == 'modified')
                    {
                        item_map[item.id] = item.data();
                    }
                }
            )

            // UPDATE TAMPILAN DAN RESET TEXTFIELDS
            loadUI();
            M.updateTextFields();

        }
    );
}

function loadUI()
{
    // CLEAR ISI
    $("#isiShop").html('');

    for(item in item_map)
    {
        if(item_map[item].forSale == true)
        {
            let harga = Math.floor(item_map[item].harga * item_map[item].mult);
            $("#isiShop").append
            (
                `
                    <div class='col s4' id='item_${item}'><img src="images/${item_map[item].nama.toLowerCase()}.png" style="width:50px;"alt="noimg" class="circle"><div class='input-field inline'> <p id='stock_dan_harga_${item}'>Stock: ${item_map[item].stock} | Harga : ${harga}</p>
                        <input type="number" name="${item_map[item].nama}" id='${item}' value=0 min=0 max=${item_map[item].stock} class='dihitung' harga-satuan="${harga}" stock='${item_map[item].stock}'>
                        <label for="${item_map[item].nama}">${item_map[item].nama}</label>
                    </div></div>
                `
            );
        }
    }

}

function buy(){
    const db = firebase.firestore();
    const labelUang = document.querySelector('#uangRegu');
    let uang = Number(labelUang.textContent);
    const labelRegu = document.querySelector('#namaRegu');
    let namaRegu = labelRegu.textContent;
    var yangDibeli = document.querySelectorAll('.dihitung');
    var total = 0;
    var added = [];
    yangDibeli.forEach((HTMLElement)=>{
        let val = Number(HTMLElement.value);
        let max = Number(HTMLElement.max);
        let harga = Number(HTMLElement.getAttribute('harga-satuan'));
        if (max < val){
            M.toast({html: `Not enough stock in ${HTMLElement.name}`, classes: 'rounded'});
        }
        else{
            if (val > 0){
                added.push({ 'nama':HTMLElement.name, 'jumlah': val, 'satuan':harga });
                total = total + val * harga;
            }
        }
    });
    if (total > Number(uang)){
        M.toast({html: `Total is ${total}. Not enough money`, classes: 'rounded'});
    } else {
        const tim = firebase.firestore().collection('teams').doc(namaRegu);
        resourceTim = {};
        tim.get().then( (snapshot)=>{
            if (snapshot.hasPendingWrites){
                // 
            } else {
                console.log(snapshot.data());
                resourceTim = snapshot.data().resource;
                if (typeof resourceTim !== 'undefined'){}
                else {
                    resourceTim = {};
                }
                console.log(resourceTim);
                dataBaru = {}
                added.forEach((param)=>{
                    //untuk setiap barang yang bisa dibeli
                    let key = `resource.`+param[`nama`];
                    dataBaru[key] = firebase.firestore.FieldValue.increment(parseInt(param['jumlah']));
                    //ngurangin stok market
                    resourceRef.where('nama','==',param['nama']).get().then ((querySnapshot)=>{
                            querySnapshot.forEach( (doc)=>{
                                resourceRef.doc(doc.id).update({
                                    stock : doc.data().stock - param['jumlah']
                                }).catch((err)=>{
                                    M.toast({html:`Error update ${err}`, classes: 'rounded'});
                                });
                            } )
                    });
                    
                });

                console.log(dataBaru);
                tim.update({money: firebase.firestore.FieldValue.increment(-1*total)});

                //update isi data tim
                tim.update(dataBaru).then(()=>{
                    write_log(`Team ${namaRegu} Membeli Item`, {items: added});
                    M.toast({html: `Items bought!. Total is ${total}. Remaining money : ${uang-total}`, classes: 'rounded'});
                }).catch((err)=>{
                    M.toast({html:`Firebase Error: ${err}`, classes: 'rounded'});
                }).finally(
                    ()=>{
                        ShowMain();
                    }
                )
            }
        } );
    }
    console.log(added, total);
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
                        team_map[team.id] = team.data();
                        
                    }
                    else if(team_obj.type == 'modified')
                    {
                        // Something's modified at team collection... (Money, Items, Resource, etc)
                        team_map[team.id] = team.data();
                        
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