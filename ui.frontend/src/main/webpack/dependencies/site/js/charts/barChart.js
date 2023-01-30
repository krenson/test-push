try {
  var colors = [
    "rgb(47, 58, 72)",
    "rgb(251, 231, 203)",
    "rgb(255, 142, 151)",
    "rgb(255, 255, 255)",
    "rgb(222, 199, 224)",
    "rgb(150, 156, 163)",
    "rgb(252, 219, 173)",
  ];

  var colorBorders = [
    "rgb(47, 58, 72)",
    "rgb(251, 231, 203)",
    "rgb(255, 142, 151)",
    "rgb(54, 64, 75)",
    "rgb(222, 199, 224)",
    "rgb(150, 156, 163)",
    "rgb(54, 64, 75)",
  ];

  const legendMargin = {
    id: "legendMargin",
    beforeInit(chart, legend, options) {
      const fitValue = chart.legend.fit;
      chart.legend.fit = function fit() {
        fitValue.bind(chart.legend)();
        return (this.height += 30);
      };
    },
  };

  // BAR CHART EXAMPLE

  var myChart = document.getElementById("myChart");

  var chartConfig = {
    type: "bar",
    data: {
      labels: [
        "Temps plein&jour/continu",
        "Temps plein à&pauses/Nuit/&W-E",
        "Temps&Partiel/4/5e",
        "Autres ou&inconnu",
      ],
      datasets: [
        {
          label: "Agent/Agente d’entretien des parcs et jardins",
          data: [90, 0.5, 5, 1],
          fill: false,
          backgroundColor: colors[0],
          borderColor: colorBorders[0],
        },
        {
          label: "Tous les métiers",
          data: [65, 15, 25, 5],
          fill: false,
          backgroundColor: colors[1],
          borderColor: colorBorders[1],
        },
      ],
    },
    options: {
      plugins: {
        tooltip: {
          titleAlign: "center",
          padding: 20,
          displayColors: false,
          backgroundColor: "#ffffff",
          footerColor: "blue",
          titleColor: "#2f3a48",
          bodyColor: "#2f3a48",
          borderColor: "#fbe7cb",
          borderWidth: 1,
          callbacks: {
            title: function (tooltipItem) {
              let tooltipTitle = tooltipItem[0].label.split("&").join(" ");
              return tooltipTitle;
            },

            label: function (tooltipItem) {
              let percentage;
              let sum;

              if (tooltipItem.datasetIndex == 0) {
                percentage = tooltipItem.raw;
                let sumFunction = () => {
                  let index =
                    chartConfig.data.datasets[0].data.indexOf(percentage);
                  return chartConfig.data.datasets[1].data[index];
                };

                sum = sumFunction();
              } else {
                sum = tooltipItem.raw;
                percentageFunction = () => {
                  let index = chartConfig.data.datasets[1].data.indexOf(sum);
                  return chartConfig.data.datasets[0].data[index];
                };

                percentage = percentageFunction();
              }

              return [
                percentage + " % pour ce métier",
                sum + " % pour tous les métiers",
              ];
            },
          },
        },
        legend: {
          align: "center",

          labels: {
            font: {
              family: "Poppins",
              size: 15,
            },
          },
        },
      },
      layout: {
        padding: {
          left: 10,
        },
      },
      indexAxis: "y",
      scales: {
        y: {
          grid: {
            display: false,
            borderColor: "rgb(219, 202, 175)",
            borderWidth: 2,
          },
          ticks: {
            font: {
              family: "Poppins",
              size: 13,
            },
            callback: function (value) {
              var valueLegend = this.getLabelForValue(value);

              var arr = valueLegend.split("&");
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
            font: {
              size: 15,
            },
          },
        },
      },
    },

    plugins: [legendMargin],
  };

  var chart = new Chart(myChart, chartConfig);

  Chart.defaults.font.family = "Poppins";
  Chart.defaults.font.size = 15;

  // Chart details

  var detailsBtn = document.getElementById(
    "accordion-2098845c33-item-ace86c638b-button"
  );

  var detailsCloseBtn = document.querySelector(".details-close");

  detailsCloseBtn.addEventListener("click", () => {
    detailsBtn.click();
  });
} catch (error) {
  console.log(error);
}
