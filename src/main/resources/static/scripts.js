function search() {
  let filter, table, tr, a, i, txtValue;
  const input = document.getElementById('myInput');
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName('tr');

  if(input.value.length < 1){
    table.style.display = "none";
  } else {
    table.style.display = "";
  }

  for (i = 0; i < tr.length; i++) {
    a = tr[i].getElementsByTagName("a")[0];
    txtValue = a.textContent || a.innerText;
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      tr[i].style.display = "";
    } else {
      tr[i].style.display = "none";
    }
  }
}


function gallery(imgs) {
  var expandImg = document.getElementById("expandedImg");
  expandImg.src = imgs.src;
  expandImg.parentElement.style.display = "block";
}


function beforeClick(){
    let inp = document.getElementsByClassName("price");
    let i;
    for(i = 0; i < inp.length; i++){
        if(inp[i].value.length == 0) inp[i].value = "0.0";
    }
    alert(inp[2].value)
}