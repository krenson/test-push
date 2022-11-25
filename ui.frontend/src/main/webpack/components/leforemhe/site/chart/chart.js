import Chart from 'chart.js/dist/chart';

const colors = [
    "rgb(47, 58, 72)",
    "rgb(251, 231, 203)",
    "rgb(255, 142, 151)",
    "rgb(255, 255, 255)",
    "rgb(222, 199, 224)",
    "rgb(150, 156, 163)",
    "rgb(252, 219, 173)",
];

const colorBorders = [
    "rgb(47, 58, 72)",
    "rgb(251, 231, 203)",
    "rgb(255, 142, 151)",
    "rgb(54, 64, 75)",
    "rgb(222, 199, 224)",
    "rgb(150, 156, 163)",
    "rgb(54, 64, 75)",
];

const SERVLET_URL = "/content/leforemhe/graphdataservlet.json";
const ATTRIBUTE_GRAPH_DATA_PATH = "data-chartdata-path";
const ATTRIBUTE_SHOW_AS_PERCENTAGE = "data-chartdata-showaspercentage";
const ATTRIBUTE_CHART_TYPE = "data-chartdata-charttype";

const CHART = {
    HorizontalBar: 'horizontal-bar',
    VerticalBar: 'vertical-bar',
}

const getChartType = (chartType) => {
    switch (chartType) {
        case 'horizontal-bar':
            return CHART.HorizontalBar;
        case 'vertical-bar':
            return CHART.VerticalBar;
        default:
            return CHART.HorizontalBar;
    }
}

const getChartTypeOption = (chartType) => {
    switch (chartType) {
        case chartType === CHART.HorizontalBar:
            return 'bar';
        case chartType === CHART.VerticalBar:
            return 'bar';
        default:
            return 'bar';
    }
}

const renderGraph = (showAsPercentage, barChartElement, data, chartType) => {
    const resolvedChartType = getChartType(chartType);
    const chartConfig = {
        type: getChartTypeOption(resolvedChartType),
        data: {
            labels: data.labels,
            datasets: getGeneratedDataset(data.datasets)
        },
        options: {
            scaleShowValues: true,
            plugins: {
                legend: {
                    display: data.datasets.length > 1 ? true : false,
                    align: "start",
                    labels: {
                        font: {
                            family: "Poppins",
                            size: 15,
                        },
                    },
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            let label = context.dataset.label || '';
                            return `${label}: ${context.formattedValue}${showAsPercentage ? '%' : ''}`
                        }
                    }
                }
            },
            indexAxis: resolvedChartType == CHART.HorizontalBar ? "y" : "x",
            scales: {
                y: {
                    grid: {
                        display: false,
                        borderColor: "rgb(219, 202, 175)",
                        borderWidth: 2,
                    },
                    ticks: {
                        autoSkip: false,
                        font: {
                            family: "Poppins",
                            size: 13,
                        },
                        callback: function (value) {
                            const valueLegend = this.getLabelForValue(value);
                            const arr = valueLegend.split("&");
                            return arr;
                        },
                    },
                },
                x: {
                    labelMaxWidth: 20,
                    beginAtZero: true,
                    grid: {
                        color: "rgb(246, 241, 234)",
                        lineWidth: 2,
                        borderColor: "rgb(219, 202, 175)",
                        borderWidth: 2.5,
                        radius: "50%",
                    },
                    ticks: {
                        autoSkip: false,
                        stepSize: 20,
                        font: {
                            size: 15,
                        },
                        callback: function (value) {
                            return `${value}${showAsPercentage ? '%' : ''}`;
                        }
                    },
                },
            },

        },
    };
    const barChart = new Chart(barChartElement, chartConfig);
}

const hasAllData = (dataset) => {
    return dataset && dataset.labels && dataset.datasets;
}

const getGeneratedDataset = (datasetResponse) => {
    const dataset = [];
    datasetResponse.forEach((data, index) => {
        dataset.push({
            label: data.label,
            data: data.data,
            fill: false,
            backgroundColor: colors[index],
            borderColor: colorBorders[index]
        })
    });
    return dataset;
}

addEventListener('DOMContentLoaded', () => {
    let headers;
    if (location.hostname == 'localhost') {
        headers = new Headers({
            'Authorization': `Basic ${btoa('admin:admin')}`
        });
    }


    const barChartsElements = document.querySelectorAll('div[class="cmp-chart"] > canvas[class="cmp-chart--barchart"]');

    barChartsElements.forEach(barChartElement => {
        fetch(`${SERVLET_URL}?chartDataPath=${barChartElement.getAttribute(ATTRIBUTE_GRAPH_DATA_PATH)}`, { headers: headers, credentials: "include" })
            .then((response) => response.json())
            .then((jsonData) => {
                if (hasAllData(jsonData)) {
                    renderGraph(barChartElement.hasAttribute(ATTRIBUTE_SHOW_AS_PERCENTAGE) ? true : false, barChartElement, jsonData, barChartElement.getAttribute(ATTRIBUTE_CHART_TYPE));
                } else {
                    barChartElement.remove();
                }
            })
            .catch((error) => {
                console.log(error);
            });
    });
});
