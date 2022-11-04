var subCheckboxes = document.querySelectorAll("input.subOption");
var optionCheckboxes = document.querySelectorAll("input.option");
var btnVoirMetiers = document.getElementById("btnArborVoirMetiers");

for (var i = 0; i < subCheckboxes.length; i++) {
  subCheckboxes[i].onclick = function () {
    var optionId = this.classList[0];
    var option = document.getElementById(optionId);

    var checkedCount = document.querySelectorAll(
      "input." + optionId + ".subOption:checked"
    ).length;

    var mysuboptions = document.querySelectorAll(
      "input." + optionId + ".subOption"
    ).length;

    option.checked = checkedCount > 0 && checkedCount == mysuboptions;
    option.indeterminate = checkedCount > 0 && checkedCount < mysuboptions;

    btnAvailability();
  };
}

for (var i = 0; i < optionCheckboxes.length; i++) {
  optionCheckboxes[i].onclick = function () {
    var optionId = this.id;

    var option = document.getElementById(optionId);

    var optionSubs = document.querySelectorAll(
      "input." + optionId + ".subOption"
    );

    if (option.checked) {
      for (var i = 0; i < optionSubs.length; i++) {
        optionSubs[i].checked = true;
      }
    } else {
      for (var i = 0; i < optionSubs.length; i++) {
        optionSubs[i].checked = false;
      }
    }

    btnAvailability();
  };
}

function btnAvailability() {
  btnVoirMetiers.disabled = true;

  for (var i = 0; i < subCheckboxes.length; i++) {
    if (subCheckboxes[i].checked == true) {
      btnVoirMetiers.disabled = false;
      return;
    }
  }
}

function createUrlParams() {
  let urlParams = "tags="
  subCheckboxes.forEach( subCheckbox => {
    if(subCheckbox.checked === true) {
      urlParams += subCheckbox.dataset.tagname + ",";
    }
  })
  optionCheckboxes.forEach(optionCheckbox => {
    if(optionCheckbox.checked === true) {
      urlParams += optionCheckbox.dataset.tagname + ",";
    }
  })
  return urlParams;
}

function clickButton(href) {
  let urlParams = createUrlParams();
  window.location = href + ".html?" + urlParams;
}
