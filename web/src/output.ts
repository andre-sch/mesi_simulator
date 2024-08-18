import { reserveSeat, selectRoom } from "./api";
import { wrapperOf } from "./wrapper";

export interface Room {
  id: number;
  numberOfRows: number;
  seatsPerRow: number;
  seats: Seat[];
}

interface Seat {
  status: string
}

export class OutputRenderer {
  private container = document.querySelector(".app") as Element;
  private processorName: string;

  constructor(
    private processorId: number,
    private numberOfRooms: number
  ) {
    this.processorName = `processor-${processorId}`;
  }

  private outputQuery(): string { return `.output.${this.processorName}`; }

  public render(room?: Room): void {
    var output = document.querySelector(this.outputQuery());
    if (!output) this.initialize();
    if (room) this.update(this.roomPage(room));
  }

  private initialize(): void {
    var output = wrapperOf(...this.mainPage());
    output.classList.add("output", this.processorName);
    output.style.gridArea = `output-${this.processorId}`;
    this.container.appendChild(output);
  }

  private update(newContent: Element[]): void {
    var output = document.querySelector(this.outputQuery()) as Element;

    output.innerHTML = "";
    newContent.forEach(node => output.appendChild(node));
  }

  private mainPage(): Element[] {
    var header = document.createElement("h1");
    header.textContent = "Cinema";

    var options = document.createElement("section");
    options.classList.add("room-options");

    for (var roomId = 0; roomId < this.numberOfRooms; roomId++) {
      var button = document.createElement("button");
      button.className = "select-room";
      button.textContent = `Sala ${roomId}`;
      button.onclick = this.selectOption.bind(this, roomId);
      options.appendChild(button);
    }

    return [header, options];
  }

  private async selectOption(roomId: number): Promise<void> {
    var room = await selectRoom(roomId, this.processorId);
    this.update(this.roomPage(room));
  }

  private roomPage(roomData: Room): Element[] {
    return [this.goBackButton(), this.room(roomData), this.reserveButton(roomData)];
  }

  private room(roomData: Room): Element {
    var room = document.createElement("div");
    room.className = "room";
    room.style.gridTemplateRows = `repeat(${roomData.numberOfRows}, 2.5rem)`;
    room.style.gridTemplateColumns = `repeat(${roomData.seatsPerRow}, 2.5rem)`;
    this.seats(roomData).forEach(seat => room.appendChild(seat));
    return room;
  }

  private seats(roomData: Room): Element[] {
    var elements: Element[] = [];
    roomData.seats.forEach((seatData, index) => {
      var seat = document.createElement("div");
      seat.textContent = index.toString();

      var status = seatData.status.toLowerCase();
      seat.classList.add("seat", status);

      if (status == "available")
        seat.onclick = this.selectSeat.bind(this, seat);

      elements.push(seat);
    });
    return elements;
  }

  private selectSeat(seat: Element): void {
    var selectedSeat = document.querySelector(this.outputQuery() + " .seat.selected");
    if (selectedSeat) selectedSeat.classList.remove("selected");
    if (selectedSeat != seat) seat.classList.add("selected");
  }

  private goBackButton(): Element {
    var anchor = document.createElement("a");
    anchor.className = "go-back";
    anchor.textContent = "Voltar";
    anchor.onclick = this.goBack.bind(this);
    return anchor;
  }

  private goBack() { this.update(this.mainPage()); }

  private reserveButton(room: Room): Element {
    var button = document.createElement("button");
    button.className = "reserve";
    button.textContent = "Reservar";
    button.onclick = this.reserve.bind(this, room.id);
    return button;
  }

  private reserve(roomId: number): void {
    var selectedSeat = document.querySelector(this.outputQuery() + " .seat.selected");
    if (!selectedSeat) return;

    var seatId = Number(selectedSeat.textContent);
    reserveSeat(roomId, seatId, this.processorId);
    selectedSeat.classList.remove("selected");
  }
}
