import {
  numberOfProcessors,
  renderCache,
  renderMemory,
  renderOutput
} from "./api";

window.addEventListener("load", async () => {
  renderMemory();

  for (var processorId = 0; processorId < numberOfProcessors; processorId++) {
    renderCache(processorId);
    renderOutput(processorId);
  }
})
