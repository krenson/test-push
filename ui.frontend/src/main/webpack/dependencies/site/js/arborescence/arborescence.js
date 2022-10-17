var subCheckboxes = document.querySelectorAll("input.subOption");
var optionCheckboxes = document.querySelectorAll("input.option");

for (var i = 0; i < subCheckboxes.length; i++) {
  subCheckboxes[i].onclick = function () {
    var optionId = this.classList[0];
    var option = document.getElementById(optionId);

    var checkedCount = document.querySelectorAll(
      "input." + optionId + ".subOption:checked"
    ).length;

    option.checked = checkedCount > 0;
    option.indeterminate =
      checkedCount > 0 && checkedCount < subCheckboxes.length;
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
  };
}
