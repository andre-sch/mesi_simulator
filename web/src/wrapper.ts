function wrapperOf(...nodeList: Element[]): HTMLDivElement {
  var wrapper = document.createElement("div");
  
  wrapper.classList.add("wrapper");
  nodeList.forEach(node => wrapper.appendChild(node));

  return wrapper;
}

export { wrapperOf };