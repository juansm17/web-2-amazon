function $(id){
  return document.getElementById(id);
}

function register() {
  const data = {
    name:$('name').value,
    lastName:$('lastName').value,
    username:$('user').value,
    email:$('email').value,
    password:$('password').value,
  },
  params = {
    method: "POST", 
    headers: new Headers({'Content-Type': 'application/json'}), 
    body:JSON.stringify(data) 
  }
  fetch("./../register", params)
  .then(resp => resp.json())
  .then(resp => {
    console.log(resp);
    if (resp.status === 200){
      location.href = "./../views/login.html";
    } else {
      M.toast({html: resp.message+", status("+resp.status+")", inDuration:500, outDuration:500 })
    }
  });
}


