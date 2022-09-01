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

// BAR CHART EXAMPLE

var myBarChart = document.getElementById("myBarChart");

var barChartConfig = {
  type: "bar",
  data: {
    labels: [
      "Temps partiel",
      "temps plein a p...",
      "temps plein jour",
      "autres ou inconnu",
    ],
    datasets: [
      {
        label: "Agent d'entretien des parcs et jardins",
        data: [90, 40, 170, 40],
        fill: false,
        backgroundColor: colors[0],
        borderColor: colorBorders[0],
      },
      {
        label: "Tous les métiers",
        data: [100, 30, 160, 80],
        fill: false,
        backgroundColor: colors[1],
        borderColor: colorBorders[1],
      },
    ],
  },
  options: {
    scales: {
      y: {
        grid: {
          color: "rgb(246, 241, 234)",
          lineWidth: 2,
        },
        max: 300,
        beginAtZero: true,
        ticks: {
          stepSize: 100,
          fontsize: 40,
        },
      },
      x: {
        grid: {
          display: false,
        },
        ticks: {
          autoSkip: false,
        },
      },
    },
  },
};

var barChart = new Chart(myBarChart, barChartConfig);
Chart.defaults.font.family = "Poppins";
Chart.defaults.font.size = 15;

// Chart details

var detailsBtn = document.getElementById(
  "accordion-2098845c33-item-ace86c638b-button"
);

var detailsCloseBtn = document.getElementById("close-container");

detailsCloseBtn.addEventListener("click", () => {
  detailsBtn.click();
});
