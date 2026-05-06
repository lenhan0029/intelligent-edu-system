import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ChatMessage {
  id?: number;
  senderId: number;
  recipientId: number;
  content: string;
  sentAt?: string;
  organizationId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private stompClient: Client | null = null;
  private messageSubject = new BehaviorSubject<ChatMessage | null>(null);
  public message$ = this.messageSubject.asObservable();

  private apiUrl = `${environment.apiUrl}/notification/api/chat`;

  constructor(private http: HttpClient) {}

  public connect(userId: number) {
    const socket = new (SockJS as any)(`${environment.apiUrl}/notification/ws-chat`);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      this.stompClient?.subscribe(`/user/${userId}/queue/messages`, (message: Message) => {
        if (message.body) {
          this.messageSubject.next(JSON.parse(message.body));
        }
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.stompClient.activate();
  }

  public disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  public sendMessage(chatMessage: ChatMessage) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/app/chat',
        body: JSON.stringify(chatMessage)
      });
    }
  }

  public getHistory(userId1: number, userId2: number): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.apiUrl}/history/${userId1}/${userId2}`);
  }
}
