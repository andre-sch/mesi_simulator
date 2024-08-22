import axios from "axios";
import { MemoryRenderer } from "./memory";
import { CacheRenderer, Line } from "./cache";
import { Room, OutputRenderer } from "./output";

var api = axios.create({
  baseURL: "http://localhost:8080"
});

var numberOfProcessors = 3;
var renderedCaches = new Map<number, number>();

var response = await api.get<{ count: number }>(`/rooms/count`);
var roomCount = response.data.count;

async function renderMemory(): Promise<void> {
  var memoryRenderer = new MemoryRenderer();
  var content = await getMemoryContent();
  memoryRenderer.render(content);
}

async function renderCache(id: number): Promise<void> {
  var cacheRenderer = new CacheRenderer(id);
  var content = await getCacheContent(id);
  cacheRenderer.render(content);
}

async function createProcessor(): Promise<number> {
  var response = await api.post<{ id: number }>("/processors");
  return response.data.id;
}

async function renderOutput(processorId: number, newContent?: Room) {
  var outputRenderer = new OutputRenderer(processorId, roomCount);
  outputRenderer.render(newContent);
}

async function selectRoom(roomId: number, processorId: number): Promise<void> {
  var response = await api.put<Room>(`/rooms/${roomId}`, { processorId });
  var room = response.data;

  renderRoomUpdate(room, processorId);
}

async function reserveSeat(roomId: number, seatId: number, processorId: number): Promise<void> {
  var response = await api.put<Room>(`/rooms/${roomId}/seats/${seatId}/reserve`, { processorId });
  var room = response.data;

  renderRoomUpdate(room, processorId);
}

function renderRoomUpdate(room: Room, processorId: number): void {
  renderMemory();

  renderedCaches.set(processorId, room.id);
  renderCachesRelatedToRoom(room.id);

  renderOutput(processorId, room);
}

function renderCachesRelatedToRoom(roomId: number) {
  renderedCaches.forEach((renderedRoomId, renderedProcessorId) => {
    if (renderedRoomId == roomId) renderCache(renderedProcessorId);
  });
}

async function getCacheContent(id: number): Promise<Line[]> {
  var response = await api.get<Line[]>(`/caches/${id}`);
  return response.data;
}

async function getMemoryContent(): Promise<number[]> {
  var response = await api.get<number[]>("/shared-memory");
  return response.data;
}

export {
  numberOfProcessors,
  renderMemory,
  renderCache,
  createProcessor,
  renderOutput,
  selectRoom,
  reserveSeat
};