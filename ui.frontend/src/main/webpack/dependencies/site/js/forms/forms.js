// JavaScript Document
var resident = document.querySelector("#resident");
var belge = document.querySelector("#belge");
var entite = document.querySelector("#entite");
var libre = document.querySelector("#libre");
var memoire = document.querySelector("#memoire");
var connaissance = document.querySelector("#connaissance");
var demandeEntr = document.querySelector("#demandeEntr");
var email = document.querySelector("#email");
var confirmation = document.querySelector("#confirmation");
var demandePartic = document.querySelector("#demandePartic");
var emailEntr = document.querySelector("#emailEntr");
var confirmationEntr = document.querySelector("#confirmationEntr");
var objetEntr = document.querySelector("#objetEntr");

//resident
if (resident != null) {
  resident.addEventListener("change", function (e) {
    const niss = document.getElementById("niss");
    var value = e.target.value;

    if (niss === null) return;

    if (value == "oui") {
      niss.setAttribute("required", "required");
      niss.setAttribute("aria-required", "true");
    } else {
      niss.removeAttribute("required");
      niss.setAttribute("aria-required", "false");
    }
  });
}
//belge
if (belge != null) {
  belge.addEventListener("change", function (e) {
    var value = e.target.value;
    const NE = document.getElementById("NE");
    const entite = document.getElementById("entite");
    const entity = document.getElementById("entity");
    const num_entre = document.getElementById("num_entre");
    const num_entre_etr = document.getElementById("num_entre_etr");

    if (NE === null) return;
    if (entite === null) return;
    if (entity === null) return;
    if (num_entre === null) return;
    if (num_entre_etr === null) return;

    if (value == "oui") {
      NE.setAttribute("required", "required");
      NE.setAttribute("aria-required", "true");
      entite.setAttribute("required", "required");
      entite.setAttribute("aria-required", "true");

      for (var i = 0; i < entity.children.length; i++) {
        entity.children[i].classList.remove("display-none");
      }

      for (var i = 0; i < num_entre.children.length; i++) {
        num_entre.children[i].classList.remove("display-none");
      }

      for (var i = 0; i < num_entre_etr.children.length; i++) {
        num_entre_etr.children[i].classList.remove("display-none");
      }
    } else {
      NE.removeAttribute("required");
      NE.setAttribute("aria-required", "false");
      entite.removeAttribute("required");
      entite.setAttribute("aria-required", "false");

      for (var i = 0; i < entity.children.length; i++) {
        entity.children[i].classList.add("display-none");
      }

      for (var i = 0; i < num_entre.children.length; i++) {
        num_entre.children[i].classList.add("display-none");
      }

      for (var i = 0; i < num_entre_etr.children.length; i++) {
        num_entre_etr.children[i].classList.add("display-none");
      }
    }
  });
}

//entite
if (entite != null) {
  entite.addEventListener("change", function (e) {
    var value = e.target.value;
    const NE = document.getElementById("NE");

    if (NE === null) return;

    if (value == "1") {
      NE.setAttribute("pattern", "[2]\\.[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}");
      NE.setAttribute("maxlength", "13");
      NE.setAttribute("title", "Veuillez indiquer ce format : 2.xxx.xxx.xxx");
    } else {
      NE.setAttribute("pattern", "[0][0-9]{3}\\.[0-9]{3}\\.[0-9]{3}");
      NE.setAttribute("maxlength", "12");
      NE.setAttribute("title", "Veuillez indiquer ce format : 0xxx.xxx.xxx");
    }
  });
}

//libre
if (libre != null) {
  libre.addEventListener("change", function (e) {
    const etat = document.getElementById("etat");
    var value = e.target.value;

    if (etat === null) return;

    if (value == "oui") {
      etat.setAttribute("required", "required");
      etat.setAttribute("aria-required", "true");
    } else {
      etat.removeAttribute("required");
      etat.setAttribute("aria-required", "false");
    }
  });
}

//memoire
if (memoire != null) {
  memoire.addEventListener("change", function (e) {
    const sujet = document.getElementById("sujet");
    var value = e.target.value;

    if (sujet === null) return;

    if (value == "oui") {
      sujet.setAttribute("required", "required");
      sujet.setAttribute("aria-required", "true");
    } else {
      sujet.removeAttribute("required");
      sujet.setAttribute("aria-required", "false");
    }
  });
}

//libre
if (connaissance != null) {
  connaissance.addEventListener("change", function (e) {
    const autre = document.getElementById("autre");
    var value = e.target.value;

    if (autre === null) return;

    if (value == "AutreLangue") {
      autre.setAttribute("required", "required");
      autre.setAttribute("aria-required", "true");
    } else {
      autre.removeAttribute("required");
      autre.setAttribute("aria-required", "false");
    }
  });
}

//demandeEntr
if (demandeEntr != null) {
  //objetEntr
  if (objetEntr != null) {
    const condition = document.querySelector("#condition-utilisation");
    objetEntr.addEventListener("change", function (e) {
      var value = e.target.value;
      if (condition === null) return;
      if (value == "e5" || value == "e6") {
        condition.classList.remove("element-invisible");
        document
          .querySelector("#condition")
          .setAttribute("required", "required");
        document
          .querySelector("#condition")
          .setAttribute("aria-required", "true");
      } else {
        condition.classList.add("element-invisible");
        document.querySelector("#condition").removeAttribute("required");
        document
          .querySelector("#condition")
          .setAttribute("aria-required", "false");
      }
    });
  }

  demandeEntr.addEventListener("submit", function (e) {
    const emailEntr = document.getElementById("emailEntr");
    const confirmationEntr = document.getElementById("confirmationEntr");

    if (emailEntr === null) return;
    if (confirmationEntr === null) return;

    if (emailEntr.value != confirmationEntr.value) {
      confirmationEntr.focus();
      alert(
        "L'adresse e-mail de confirmation ne correspond pas à celle que vous nous avez communiquée ci-avant."
      );
      e.preventDefault();
      return false;
    }
  });
}
//emailEntr,confirmationEntr
if (emailEntr != null && confirmationEntr != null) {
  emailEntr.addEventListener("paste", (e) => {
    e.preventDefault();
  });
  confirmationEntr.addEventListener("paste", (e) => {
    e.preventDefault();
  });
}
//demandePartic
if (demandePartic != null) {
  demandePartic.addEventListener("submit", function (e) {
    const email = document.getElementById("email");
    const confirmation = document.getElementById("confirmation");

    if (email === null) return;
    if (confirmation === null) return;

    if (email.value != confirmation.value) {
      confirmation.focus();
      alert(
        "L'adresse e-mail de confirmation ne correspond pas à celle que vous nous avez communiquée ci-avant."
      );
      e.preventDefault();
      return false;
    }
  });
}
//email,confirmation
if (email != null && confirmation != null) {
  email.addEventListener("paste", (e) => {
    e.preventDefault();
  });
  confirmation.addEventListener("paste", (e) => {
    e.preventDefault();
  });
}

var inputs = document.querySelectorAll(".inputFile input[type='file']");
console.log(inputs);
inputs.forEach((element) => {
  var container = document.createElement("div");

  container.classList.add("files");
  element.parentElement.classList.add("empty");

  element.parentElement.append(container);
  element.addEventListener("change", function (e) {
    if (e.target.files.length > 0) {
      var currentContainer = e.target.parentElement;
      element.setCustomValidity("");
      var list = currentContainer.querySelector(".files");
      list.innerHTML = "";
      currentContainer.classList.remove("empty");
      var files = [...e.target.files];
      files.forEach((file) => {
        var fileToAdd = document.createElement("div");
        console.log(file);
        fileToAdd.classList.add("file");
        fileToAdd.classList.add("chips");
        var content = document.createElement("div");
        var warning = "";
        if (file.size > 5242880) {
          fileToAdd.classList.add("chips-wrong");
          warning = "Taille trop grande";
          content.classList.add("invalid");
          currentContainer
            .querySelector("input[type='file']")
            .setCustomValidity("Invalid field.");
        } else if (
          file.name.split(".").pop() != "pdf" &&
          file.name.split(".").pop() != "doc" &&
          file.name.split(".").pop() != "docx" &&
          file.name.split(".").pop() != "jpg" &&
          file.name.split(".").pop() != "png" &&
          file.name.split(".").pop() != "gif"
        ) {
          fileToAdd.classList.add("chips-wrong");
          content.classList.add("invalid");
          warning = "Ce format n’est pas conforme";
          currentContainer
            .querySelector("input[type='file']")
            .setCustomValidity("Invalid field.");
        }

        fileToAdd.classList.add("chips-secondary");
        fileToAdd.innerHTML = file.name;

        content.append(fileToAdd);
        var warningContent = document.createElement("div");
        warningContent.classList.add("warning");
        warningContent.innerHTML = warning;
        content.append(warningContent);
        content.classList.add("content");
        currentContainer.querySelector(".files").append(content);
        fileToAdd.addEventListener("click", function (e) {
          removeFile(e, currentContainer);
        });
      });
    }
  });
});

function removeFile(e, container) {
  e.preventDefault();
  container.querySelector("input[type='file']").files = null;
  container.querySelector("input[type='file']").value = null;
  var files = container.querySelector(".files");
  files.innerHTML = "";
  container.classList.add("empty");

  /* var files = [...container.querySelector("input[type='file']").files];
  var index;

  for (i = 0; i <= files.length - 1; i++) {
    if (files[i].name == e.target.innerHTML) {
      index = i;
    }
  }
  console.log(files);
  files.splice(index, 1);
  container.querySelector("input[type='file']").files = files;
  e.target.parentElement.removeChild(e.target);

  console.log(index); */
}
