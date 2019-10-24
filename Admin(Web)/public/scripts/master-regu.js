const db = firebase.firestore();
var teamRef = db.collection('teams');
var productRef = db.collection('product');

const display = document.querySelector('#display-data');

function hapus(param)
{
    M.toast({html: `ga boleh delete sembarangan`, classes: 'rounded'});
}

function update(id)
{
    let elem = document.querySelector('#modalUpdate');
    document.querySelector('#namaRegu').innerHTML = id;
    document.querySelector('#updateUang').value = document.querySelector('#'+id+' .money').innerHTML;
    elem = M.Modal.getInstance(elem);
    elem.open();
    //selector querySelector tak ubah id (unik, hasil e pasti 1)
    //attribute yg dipake untuk confirm idTim
    document.querySelector('#btnOK').setAttribute('idTim', id);
}

function confirm(e)
{
    teamRef.doc(e.getAttribute('idTim')).update
    (
        {
            money:Number(document.querySelector('#updateUang').value)
        }
    )
    .then
    (
        function()
        {
            loadTableTeams();
            M.toast({html: `Success Update ${e.getAttribute('idTim')}`, classes: 'rounded'});
        }
    )
    .catch
    (
        function(e)
        {
            M.toast({html: `Error, ${e}`, classes: 'rounded'});
        }
    );
}

function loadTableTeams()
{
    var tableTeamsBody = $("#bodyTableTeams");
    tableTeamsBody.html("");
    teamRef.get().then
    (
        function(snapshot)
        {
            snapshot.forEach
            (
                function(team)
                {
                    tableTeamsBody.append
                    (
                        `
                        <tr id='${team.id}'>
                            <td>${team.id}</td>
                            <td>${team.data().poin}</td>
                            <td class='money'>${team.data().money}</td>
                            <td>
                                <button class='btn waves-effect red' onclick='hapus("${team.id}")'>Hapus</button>
                                <button class='btn waves-effect' onclick=update("${team.id}")>Update</button>
                            </td>
                        </tr>
                        `
                    );
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
            $("#loaderTableTeams").remove();
            $("#tableTeams").show();
        }
    )
}

document.addEventListener('DOMContentLoaded', 
    function()
    {
        loadTableTeams();
        M.AutoInit();
    }
);

function ResetStanding(){
    teamRef.get().then(
        (qSnapshot)=>{
            qSnapshot.forEach(
                (data)=>{
                    teamRef.doc(data.id).update(
                        {
                            poin:0, money:200       
                        }
                    );
                }
            );
        loadTableTeams();
        }
    );
}