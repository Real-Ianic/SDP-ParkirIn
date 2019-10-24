const db = firebase.firestore();
var resourceRef = db.collection('resource');
var productRef = db.collection('product');

function deleteRes(id){
    if (confirm("Yakin dihapus?")){
        resourceRef.doc(id).delete().then(function(){
            alert('Sukses');
        }).catch(
            function(err){
                console.log(err);
            }
        );
    }
};

function hargaRes(e, id){
    resourceRef.doc(id).update(
        {
            harga: Number(e.value)
        }
    );
};

function multRes(e, id)
{
    resourceRef.doc(id).update
    (
        {
            mult: Number(e.value)
        }
    ).then(()=>{
        M.toast({html:'Multiplier Updated', classes: 'rounded'});
    });
};

function updateProd(id){
    // console.log(document.querySelector(`#harga${id}`).value);
    // console.log(document.querySelector(`#mult${id}`).value);
    productRef.doc(id).update(
        {
            mult: Number(document.querySelector(`#mult${id}`).value),
            harga: Number(document.querySelector(`#harga${id}`).value)
        }
    )
}

function sale(id){
    resourceRef.doc(id).update({
        forSale : true
    });
}

// SET STATUS ITEMS
function setStatus(id, namaresource)
{
    var cbox = $("#cb_"+id);
    if(cbox[0].checked)
    {
        M.toast({html: `${namaresource} is now On Sale`, classes: 'rounded'});
        sale(id);
    }
    else
    {
        M.toast({html: `${namaresource} is now Off Sale`, classes: 'rounded'});
        resourceRef.doc(id).update({forSale:false});
    }
}

function UpdateResource (id){
    var stokBaru = prompt('Masukkan Stok Baru: ');
    if (stokBaru != null){
        resourceRef.doc(id).update({
            stock : Number(stokBaru)
        }).then(()=>{M.toast({html:"Sukses"})})
        .catch((error)=>{console.log(error)})
    }
}

document.addEventListener('DOMContentLoaded', function(){
    var banyakKolom = 1;
    const btnInsert = document.querySelector('#insertRes');
    const cbboxJenis = document.querySelector('#jenisRes');
    const recipe = document.querySelector('#munculKalauProduct');
    const btnAdd = document.querySelector('#addBahan');
    const btnRemove = document.querySelector('#removeBahan');
    const btnClearAll = document.querySelector('#clearAll');
    const panel = document.querySelector('#appendHere');
    const harga = document.querySelector('#hargaJual');
    const stok = document.querySelector('#inputStok');

    cbboxJenis.selectedIndex=0;

    recipe.setAttribute('style', 'display:none');
    var pilihan = cbboxJenis.options[cbboxJenis.selectedIndex].value;
    btnInsert.addEventListener('click', function(e){
        //insert ke database di sini
        e.preventDefault();
        if (pilihan === "product"){
            var assoc = new Map();
            for (let i = 1; i <= banyakKolom; i++) {
                let tempNama = document.querySelector('#bahan'+i).value;
                let tempJumlah = document.querySelector('#jumlah'+i).value;
                assoc.set(tempNama, tempJumlah);
            }
            var jsonData = {};
            assoc.forEach((value, key, map) => {
                jsonData[key] = Number(value);
            });
            // console.log(jsonData);
            let dokumenRef = productRef.doc();
            dokumenRef.set({
                nama : document.querySelector('#namaRes').value,
                harga : Number(harga.value),
                bahan : jsonData
            })
            .then((param)=>{})
            .catch((err)=>{
                console.error(err);
            })
        } else if (pilihan === "rawmat") {
            console.log('Nambah di resource');
            console.log(`${document.querySelector('#namaRes').value}, ${harga.value}`);
            resourceRef.add({
                nama : document.querySelector('#namaRes').value,
                harga: Number(harga.value),
                mult: 1,
                stock: Number(stok.value)
            }).then(
                function (e) {
                    console.log('sukses', e.id);
                }
            ).catch(
                function (err) {
                    console.error("error message: ", err);
                }
            );
        } else {
            alert('pilih jenis dulu');
        }

        // console.log(e);

    });

    cbboxJenis.addEventListener('change', function(e){
        //console.log(e);
        pilihan = cbboxJenis.options[cbboxJenis.selectedIndex].value;
        if (pilihan === 'product'){
            recipe.setAttribute('style', 'display:block');
        } else {
            recipe.setAttribute('style', 'display:none');
        }
    });

    btnAdd.addEventListener('click', function(e){
        e.preventDefault();
        banyakKolom++;
        let node = document.createElement('DIV');
        node.className = 'row';
        let col1 = document.createElement('DIV');
        col1.className = 'col s6';
        let col2 = document.createElement('DIV');
        col2.className = 'col s6';

        col1.innerHTML = `<input type="text" id='bahan${banyakKolom}' placeholder='Nama Resource'>`
        col2.innerHTML = `<input type="number" min=0 placeholder='Jumlah' id='jumlah${banyakKolom}'>`
        node.appendChild(col1);
        node.appendChild(col2);

        panel.appendChild(node);
    });

    btnRemove.addEventListener('click', function(e){
        e.preventDefault();
        if(banyakKolom>1){
            banyakKolom--;
            panel.removeChild(panel.lastChild);
        };
    });

    btnClearAll.addEventListener('click', function(e){
        e.preventDefault();
        while(banyakKolom>1){
            banyakKolom--;
            panel.removeChild(panel.lastChild);
        };
    });

    resourceRef.onSnapshot(function(snapshots){
        var tabelResource = document.querySelector('#isiResources');
        tabelResource.innerHTML = '';
        snapshots.forEach( (e)=>{
            status = '';
            if (e.data().forSale) status = ' checked ';
            tabelResource.innerHTML += 
            `
                <tr>
                    <td><img src="images/${e.data().nama.toLowerCase()}.png" alt="noimg" class="circle" style="width:50px;"></td>
                    <td>${e.data().nama}</td>
                    <td><input onchange="hargaRes(this,'${e.id}')" type='number' value="${e.data().harga}"></td>
                    <td><input onchange="multRes(this, '${e.id}')" type='number' value="${e.data().mult}"></td>
                    <td><div class='switch'><label><input type='checkbox' ${status} onchange='setStatus("${e.id}","${e.data().nama}")' id='cb_${e.id}'><span class='lever'></span></label></div></td>
                    <td>${e.data().stock}</td>
                    <td><button class='btn red' onclick=deleteRes('${e.id}')>Delete</button> <button class='btn' onclick=UpdateResource('${e.id}')>Update Stock</button></td>
                </tr>
            `;
            //<td>${e.id}</td>
            //obsolete, ganti pakek string interpolation
            // tabelResource.innerHTML += "<tr> <td>"+e.id+"</td> <td>"+
            // e.data().nama+"</td> <td> <input onchange=hargaRes(this,'"+e.id+"') type='number' value="+e.data().harga+
            // "> </td> 
            // "<td> <input onchange=multRes(this,'"+e.id+"') type='number' value="+e.data().mult+"></td><td><div class='switch'><label><input type='checkbox'"+status+"onchange='setStatus(\""+ e.id+"\",\""+e.data().nama+"\")' id='cb_"+e.id+"'><span class='lever'></span></label></div></td><td><button class='btn red' onclick=deleteRes('"+e.id+"')>Delete</button></td> </tr>";
        } );
    });

    productRef.onSnapshot(function(snapshots){
        var tabelProduct = document.querySelector('#isiProduct');
        tabelProduct.innerHTML = '';
        snapshots.forEach( (e)=>{
            status = '';
            tabelProduct.innerHTML += `<tr> <td><img src="images/${e.data().nama.toLowerCase()}.png" alt="noimg" class="circle" style="width:50px;"></td> <td>${ e.data().nama}</td> <td> <input id='harga${e.id}' type='number' value=${e.data().harga}> </td> <td> <input id='mult${e.id}' type='number' value=${e.data().mult}></td> <td><button class='btn' onclick=updateProd('${e.id}')>Update</button></td> </tr>`;
        } );
    });
    
    M.AutoInit();
});


function start() {
    // body...
    resourceRef.get().then(
        (qSnapshot)=>{
            qSnapshot.forEach(
                (doc)=>{
                    resourceRef.doc(doc.id).update(
                        {stock:Number(doc.data().maksStok/5)}
                    );
                }
            );
        }
    );
}

function full() {
    // body...
    resourceRef.get().then(
        (qSnapshot)=>{
            qSnapshot.forEach(
                (doc)=>{
                    resourceRef.doc(doc.id).update(
                        {stock:Number(doc.data().maksStok)}
                    );
                }
            );
        }
    );
}

function rounds() {
    // body...
    resourceRef.get().then(
        (qSnapshot)=>{
            qSnapshot.forEach(
                (doc)=>{
                    let jumlah = -1;
                    if (doc.data().stock < doc.data().maksStok){
                        jumlah = doc.data().maksStok/3
                        if (jumlah>50) jumlah = 50;
                        if (doc.data().stock+jumlah > doc.data().maksStok){
                            resourceRef.doc(doc.id).update(
                                {stock:Number(doc.data().maksStok)}
                            );
                        }
                        else {
                            jumlah = parseInt(jumlah);

                            resourceRef.doc(doc.id).update(
                                {stock:firebase.firestore.FieldValue.increment(jumlah)}
                            );
                        }
                    } else {
                        jumlah = 0;
                    }
                    
                }
            );
        }
    );
}