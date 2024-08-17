function wrapperOf(...nodeList: Element[]): Element {
  var wrapper = document.createElement("div");
  
  wrapper.classList.add("wrapper");
  nodeList.forEach(node => wrapper.appendChild(node));

  return wrapper;
}

export { wrapperOf };