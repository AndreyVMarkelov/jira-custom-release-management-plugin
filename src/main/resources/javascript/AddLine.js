function addLine(){
    var tableElmt = document.getElementById("config-table-main");
    var rowsNb = tableElmt.rows.length;
    rowsNb--;
    var template =
        "<td headers='name'><input id='input_"+rowsNb+"_0' name='input_"+rowsNb+"_0' type='text' class='text'  /></td>"+
            "<td headers='type'><input id='input_"+rowsNb+"_1' name='input_"+rowsNb+"_1'  type='text' class='text' /></td>"+
            "<td headers='type'><input id='input_"+rowsNb+"_2' name='input_"+rowsNb+"_2'  type='text' class='text' /></td>"+
            "<td class='action' headers='action'>"+
            "<ul class='menu' style = 'list-style-type: none; margin: 0;padding : 0;margin-top: 0;text-align: left;'>"+
            "<li>"+
            "<a href='#' onclick='removeLine(event); return false;'>Remove</a>"+
            "</li>"+
            "</ul>"+
            "</td>";

    var tableBodyElmt = document.getElementById("config-table-main").tBodies[0];
    var trTag = document.createElement("tr");
    tableBodyElmt.appendChild(trTag);
    trTag.innerHTML = template;

}
function removeLine(event){
    var target = event.currentTarget.parentNode.parentNode.parentNode.parentNode;
    var tableElmt = document.getElementById("config-table-main");
    tableElmt.deleteRow(target.rowIndex) ;
}

