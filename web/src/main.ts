import {
  numberOfProcessors,
  renderCache,
  renderMemory,
  renderOutput
} from "./api";

window.addEventListener("load", async () => {
  renderMemory();

  var layout = "";
  for (var processorId = 0; processorId < numberOfProcessors; processorId++) {
    renderCache(processorId);
    renderOutput(processorId);
    layout += `"output-${processorId} cache-${processorId} memory"\n`
  }

  var app = document.querySelector(".app") as HTMLDivElement;
  app.style.gridTemplateAreas = layout;
})
