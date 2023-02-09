var subCheckboxes = document.querySelectorAll("input.subOption");
var optionCheckboxes = document.querySelectorAll("input.option");
var btnVoirMetiers = document.getElementById("btnArborVoirMetiers");

// to keep track of checkboxes on inderterminate state
var optionInder = [];

// click on sub option
subCheckboxes.forEach((sub) => {
  sub.onclick = () => {
    var optionName = sub.classList[0];
    var option = document.querySelector(`input[name="${optionName}"]`);

    var mySuboptions = document.querySelectorAll(
      "input." + optionName + ".subOption"
    ).length;

    var checkedCount = document.querySelectorAll(
      "input." + optionName + ".subOption:checked"
    ).length;

    option.checked = checkedCount > 0 && checkedCount == mySuboptions;

    if (
      checkedCount > 0 &&
      checkedCount == mySuboptions &&
      optionInder.includes(option)
    ) {
      optionInder.pop(option);
    }

    option.indeterminate = checkedCount > 0 && checkedCount < mySuboptions;

    if (
      checkedCount > 0 &&
      checkedCount < mySuboptions &&
      !optionInder.includes(option)
    ) {
      optionInder.push(option);
    }

    btnAvailability();
  };
});

// click on option
optionCheckboxes.forEach((option) => {
  option.onclick = () => {
    var optionSubs = document.querySelectorAll(
      "input." + option.name + ".subOption"
    );

    optionSubs.forEach((sub) => {
      sub.checked = option.checked;
    });

    // when checkbos is on inderterminate state
    if (optionInder.includes(option)) {
      option.checked = false;
      optionInder.pop(option);
      uncheckAll(optionSubs);
    }

    openOption(option);

    btnAvailability();
  };
});

function uncheckAll(subs) {
  subs.forEach((sub) => {
    sub.checked = false;
  });
}

function btnAvailability() {
  btnVoirMetiers.disabled = true;

  subCheckboxes.forEach((sub) => {
    if (sub.checked == true) {
      btnVoirMetiers.disabled = false;
      return;
    }
  });
}

// open accordeon
function openOption(opt) {
  if (!opt.parentElement.parentElement.hasAttribute("data-cmp-expanded")) {
    // title button:  attributes to indicate open accordeon
    opt.parentElement.parentElement.setAttribute("data-cmp-expanded", "");

    opt.parentElement
      .querySelector("button")
      .classList.add("cmp-accordion__button--expanded");
    opt.parentElement.setAttribute("aria-expanded", "true");

    // div with the sub options: attributes to indicate open accordeon
    opt.parentElement.parentElement
      .querySelector("div.cmp-accordion__panel")
      .classList.remove("cmp-accordion__panel--hidden");

    opt.parentElement.parentElement
      .querySelector("div.cmp-accordion__panel")
      .classList.add("cmp-accordion__panel--expanded");

    opt.parentElement.parentElement
      .querySelector("div.cmp-accordion__panel")
      .setAttribute("aria-hidden", "false");
  }
}

function createUrlParams() {
  let urlParams = "or=true&tags=";
  subCheckboxes.forEach((subCheckbox) => {
    if (subCheckbox.checked === true) {
      urlParams += subCheckbox.dataset.tagname + ",";
    }
  });
  optionCheckboxes.forEach((optionCheckbox) => {
    if (optionCheckbox.checked === true) {
      urlParams += optionCheckbox.dataset.tagname + ",";
    }
  });
  return urlParams;
}

function clickButton(href) {
  let urlParams = createUrlParams();
  window.location = href + "?" + urlParams;
}
