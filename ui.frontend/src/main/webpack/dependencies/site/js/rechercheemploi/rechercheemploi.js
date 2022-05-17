(function () {

    const fetchJobCount = (link) => {

        const data = fetch(link)
            .then(resp => resp.json())
            .then(res => {
                if (res.OfferCount && res.OfferCount.numberOfOffers) {
                    return res.OfferCount.numberOfOffers;
                } else {
                    return 0;
                }
            }).catch(() => {
                return 0;
            });

        return data;
    };

    document.querySelectorAll('.searchBandJobsCV form fieldset legend span').forEach(span => {
        const link = span.getAttribute('data-link');
        fetchJobCount(link)
            .then(data => {
                span.innerHTML = data;
            });
    });

})();