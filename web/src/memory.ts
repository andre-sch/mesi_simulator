import { wrapperOf } from "./wrapper";

export class MemoryRenderer {
  private addressesPerBlock = 6;
  private container = document.querySelector(".app") as Element;

  public render(data: number[]) {
    var alreadyExists = document.querySelector(".main-memory") != null;
    if (alreadyExists) this.update(data);
    else this.initialize(data);
  }

  private update(newData: number[]) {
    var dataCells = document.querySelectorAll(".main-memory td:last-child");

    dataCells.forEach((cell, index) => {
      cell.textContent = newData[index].toString();
    })
  }

  private initialize(data: number[]) {
    var table = document.createElement("table");
    table.appendChild(this.header());
  
    data.forEach((value, address) => {
      table.appendChild(this.row(value, address));
    })

    var wrapper = wrapperOf(table);
    wrapper.classList.add("main-memory");
    this.container.appendChild(wrapper);
  }

  private header(): Element {
    var header = document.createElement("tr");
    
    header.innerHTML = `
      <th>Bloco</th>
      <th>Índice</th>
      <th>Dado</th>
    `;

    return header;
  }

  private row(value: number, memoryAddress: number): Element {
    var row = document.createElement("tr");
  
    var block = Math.floor(memoryAddress / this.addressesPerBlock);
    var blockAddress = memoryAddress % this.addressesPerBlock;

    if (blockAddress == 0)
      row.innerHTML += `<td rowspan="${this.addressesPerBlock}">${block}</td>`

    row.innerHTML += `
      <td>${blockAddress}</td>
      <td>${value}</td>
    `;

    return row;
  }
}
