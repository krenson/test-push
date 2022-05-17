

const page = document.querySelector('#ThisPage');
const points_oct = document.querySelector('#points_oct');
const valeur_point = document.querySelector('#valeur_point');
const taux_sub_moyen = document.querySelector('#taux_sub_moyen');

const temps_plein = document.querySelector('#temps_plein');
const reduc_coti = document.querySelector('#reduc_coti');
const taux_occup = document.querySelector('#taux_occup');
const total_volet_2 = document.querySelector('#total_volet_2');
const resultat = document.querySelector('#resultat');
const simulateur_ape = document.querySelector('#simulateur_ape');

const getComma = (event) => {
    let content = event.target.value;
    if (event.which == 110) {
        event.preventDefault();
        content+=",";
    }
    return event.target.value = content;
};

Number.prototype.toDecimal=function(precision){
    return Math.round(this*(Math.pow(10,precision))) /(Math.pow(10,precision));
}

matchPercent = function (value) {
    const content = value;
    const regex = /^(0*(\d{1,2}(\,\d{1,2})?)|\,\d+|100(\,0+$)?)$/;
    if (content === "") return true;
    if (!content.match(regex) || content === "0") {
        return false;
    }
    return true;
};

matchTP = function (value) {
    const content = value;
    const regex = /^\d{1,4}(\,(\d{1,2}))?$/;
    if (content === "") return true;
    if (!content.match(regex) || content === "0") {
        return false;
    }
    return true;
};

matchReducCoti = function (value) {
    const content = value;
    const regex = /^\d{1,5}(\,(\d{1,2}))?$/;

    if (content === "") return true;
    if (!content.match(regex)) {
        return false;
    }
    return true;
};

calculateVolet1 = function () {
    const points_oct = document.getElementById("points_oct");
    const valeur_point = document.getElementById('valeur_point');
    const taux_sub_moyen = document.getElementById('taux_sub_moyen');

    const A = parseFloat(points_oct.value.replace(",",".")).toFixed(2);
    const B = parseFloat(valeur_point.value.replace(",",".")).toFixed(2);
    const C = parseFloat(taux_sub_moyen.value.replace(",",".")).toFixed(2);

    let result = A*B*(C/100);

    if (isNaN(result)) {
        return null;
    }

    return result.toDecimal(2);

};

calculateVolet2 = function () {
    const temps_plein = document.getElementById("temps_plein");
    const reduc_coti = document.getElementById('reduc_coti');
    const taux_occup = document.getElementById('taux_occup');
    const var_index = document.getElementById('var_index');

    const D = parseFloat(temps_plein.value.replace(",",".")).toFixed(2);
    const E = parseFloat(reduc_coti.value.replace(",",".")).toFixed(2);
    const F = parseFloat(taux_occup.value.replace(",",".")).toFixed(2);
    const G = parseFloat(var_index.value.replace(",",".")).toFixed(4);

    let result = D*E*(F/100)*G;

    if (isNaN(result)) {
        return null;
    }

    return result.toDecimal(2);
};

matchInteger = function (value) {
    const content = value;
    const regex = /^[1-9]\d{0,3}$/;

    if (!content.match(regex)) {
        return false;
    }

    return true;
};

if ( page.value == "volet_1" ) {
    taux_sub_moyen.addEventListener('keydown', getComma);

    points_oct.addEventListener('blur', function(event) {
        let sibling = this.nextElementSibling;
        if (! matchInteger(this.value)) {
            sibling.classList.remove("element-invisible");
            setTimeout(function () { document.getElementById("points_oct").focus(); },1);
        } else {
            sibling.classList.add("element-invisible");
        }
    });

    taux_sub_moyen.addEventListener('blur', function(event) {
        const total_volet_1 = document.getElementById("total_volet_1");
        let sibling = this.nextElementSibling;
        if (! matchPercent(this.value)) {
            sibling.classList.remove("element-invisible");
            setTimeout(function () { document.getElementById("taux_sub_moyen").focus(); },1);
        } else {
            sibling.classList.add("element-invisible");
        }

        total_volet_1.value = calculateVolet1().toString().replace(".",",");
    });

    simulateur_ape.addEventListener('submit', function(e) {
        const taux_sub_moyen = document.getElementById("taux_sub_moyen");
        const points_oct = document.getElementById("points_oct");

        if (! matchPercent(taux_sub_moyen.value)) {
            taux_sub_moyen.focus();
            e.preventDefault();
            return false;
        }

        if (! matchInteger(points_oct.value)) {
            points_oct.focus();
            e.preventDefault();
            return false;
        }
    });
}

if ( page.value == "volet_2" ) {
    temps_plein.addEventListener('keydown', getComma);
    reduc_coti.addEventListener('keydown', getComma);
    taux_occup.addEventListener('keydown', getComma);

    temps_plein.addEventListener('blur', function(event) {
        let sibling = this.nextElementSibling;
        if (! matchTP(this.value)) {
            sibling.classList.remove("element-invisible");
            setTimeout(function () { document.getElementById("temps_plein").focus(); },1);
        } else {
            sibling.classList.add("element-invisible");
        }
    });

    reduc_coti.addEventListener('blur', function(event) {
        let sibling = this.nextElementSibling;
        if (! matchReducCoti(this.value)) {
            sibling.classList.remove("element-invisible");
            setTimeout(function () { document.getElementById("reduc_coti").focus(); },1);
        } else {
            sibling.classList.add("element-invisible");
        }
    });

    taux_occup.addEventListener('blur', function(event) {
        const total_volet_2 = document.getElementById("total_volet_2");
        let sibling = this.nextElementSibling;

        if (! matchPercent(this.value)) {
            sibling.classList.remove("element-invisible");
            setTimeout(function () { document.getElementById("taux_occup").focus(); },1);
        } else {
            sibling.classList.add("element-invisible");
        }
        total_volet_2.value = calculateVolet2().toString().replace(".",",");
    });

    simulateur_ape.addEventListener('submit', function(e) {
        const temps_plein = document.getElementById("temps_plein");
        const reduc_coti = document.getElementById("reduc_coti");
        const taux_occup = document.getElementById("taux_occup");

        if (! matchTP(temps_plein.value)) {
            temps_plein.focus();
            e.preventDefault();
            return false;
        }
        if (! matchReducCoti(reduc_coti.value)) {
            reduc_coti.focus();
            e.preventDefault();
            return false;
        }
        if (! matchReducCoti(taux_occup.value)) {
            taux_occup.focus();
            e.preventDefault();
            return false;
        }
    });
}