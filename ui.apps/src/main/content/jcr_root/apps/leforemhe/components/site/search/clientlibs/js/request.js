$(document).ready(function () {
    const form = document.getElementsByClassName("searchContainer")[0];
    const orCheckbox = document.getElementById("searchCheckbox");
    const urlSearch = form.dataset.searchResultPage;
    const showResults = form.dataset.showSearchResults;
    const dynamicSearch = form.dataset.dynamicSearch;
    let searchBtn = document.getElementsByClassName("searchBtn")[0];
    const checkboxes = document.querySelectorAll('.checkbox-container input');


    function doSearch() {
        if (typeof urlSearch === 'string') {
            let suggestionTerms = document.querySelectorAll(".search-suggestion-term");
            var url = '';
            suggestionTerms.forEach((item, index) => {
                if (index === 0) {
                    url += `?q=${item.innerText}`;
                } else {
                    url += `,${item.innerText}`;
                }
            });

            let tagsValue = '';
            tagsValue = getTagQuery();
            if (tagsValue !== '') {
                url += suggestionTerms.length > 0 ? `&tags=${tagsValue}` : `?tags=${tagsValue}`;
            }
            if (orCheckbox.checked) {
                url += tagsValue == '' && suggestionTerms.length < 1 ? '?or=true' : '&or=true';
            }
            window.location = urlSearch + url;
        }
    }

    function getTagQuery() {
        let tagsValue = '';
        let index = 0;
        checkboxes.forEach((checkbox) => {
            if (checkbox.checked && !tagsValue.includes(checkbox.name)) {
                tagsValue += index === 0 ? checkbox.name : ',' + checkbox.name;
                index++
            }
        })
        return tagsValue;
    }

    function initSearch() {
        if (showResults == "false" || dynamicSearch == "false") {
            searchBtn.addEventListener("click", doSearch);
        }
    }

    initSearch();

})
