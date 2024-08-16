export interface Room {
  id: number;
  numberOfRows: number;
  seatsPerRow: number;
  seats: Seat[];
}

interface Seat {
  status: string
}

export class RoomRenderer {
  private container = document.querySelector(".app") as Element;
  private id: string;

  constructor(id: number) {
    this.id = `computer-${id}`;
  }

  private outputQuery(): string { return `.output.${this.id}`; }

  public render(data: Room) {
    var output = document.querySelector(this.outputQuery());
    if (output != null) this.update(output, data);
    else this.initialize(data);
  }

  private update(output: Element, newData: Room) {
    output.innerHTML = this.content(newData);
  }

  private initialize(data: Room) {
    var output = this.output(data);
    this.update(output, data)
    this.container.appendChild(output);
  }

  private output(data: Room): Element {
    var output = document.createElement("div");
    output.classList.add("output", this.id.toString());
    output.style.gridTemplateRows = `repeat(${data.numberOfRows}, 2.5rem)`
    output.style.gridTemplateColumns = `repeat(${data.seatsPerRow}, 2.5rem)`
    return output;
  }

  private content(room: Room): string {
    var content = "";
    room.seats.forEach((seat, index) => {
      var seatRepresentation = document.createElement("div");
      seatRepresentation.classList.add("seat", seat.status.toLowerCase());
      seatRepresentation.textContent = index.toString();
      content += seatRepresentation.outerHTML;
    });
    return content;
  }
}
