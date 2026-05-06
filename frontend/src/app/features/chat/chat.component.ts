import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, ChatMessage } from '../../core/services/chat.service';
import { AuthService } from '../../core/services/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  selectedChatId: string = '1';
  currentUserId: number = 0;
  newMessage: string = '';
  private messageSubscription: Subscription | null = null;
  
  conversations = [
    { id: '1', name: 'Dr. Sarah Wilson', lastMsg: 'The new curriculum looks great!', lastTime: '10:30 AM', userId: 101 },
    { id: '2', name: 'John Student', lastMsg: 'When is the deadline for the physics project?', lastTime: 'Yesterday', userId: 102 },
    { id: '3', name: 'Admin Office', lastMsg: 'Monthly report is ready for review.', lastTime: 'Monday', userId: 103 },
    { id: '4', name: 'Parent - Maria Garcia', lastMsg: 'Thank you for the update on Lucas.', lastTime: '2 days ago', userId: 104 }
  ];

  messages: any[] = [];

  get selectedChat() {
    return this.conversations.find(c => c.id === this.selectedChatId);
  }

  constructor(
    private chatService: ChatService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user = this.authService.user();
    if (user && user.id) {
      this.currentUserId = user.id;
      this.chatService.connect(this.currentUserId);
      
      this.messageSubscription = this.chatService.message$.subscribe(msg => {
        if (msg && this.selectedChat) {
          const isRelevant = msg.senderId === this.selectedChat.userId || 
                            msg.recipientId === this.selectedChat.userId;
          
          if (isRelevant) {
            this.messages.push({
              sender: msg.senderId === this.currentUserId ? 'me' : 'them',
              content: msg.content
            });
          }
        }
      });

      this.loadHistory();
    }
  }

  ngOnDestroy(): void {
    this.messageSubscription?.unsubscribe();
    this.chatService.disconnect();
  }

  selectChat(id: string) {
    this.selectedChatId = id;
    this.loadHistory();
  }

  loadHistory() {
    if (this.selectedChat) {
      this.chatService.getHistory(this.currentUserId, this.selectedChat.userId).subscribe(history => {
        this.messages = history.map(m => ({
          sender: m.senderId === this.currentUserId ? 'me' : 'them',
          content: m.content
        }));
      });
    }
  }

  sendMessage() {
    if (this.newMessage.trim() && this.selectedChat) {
      const msg: ChatMessage = {
        senderId: this.currentUserId,
        recipientId: this.selectedChat.userId,
        content: this.newMessage,
        organizationId: this.authService.user()?.organizationId
      };
      
      this.chatService.sendMessage(msg);
      
      // Optimistic update
      this.messages.push({
        sender: 'me',
        content: this.newMessage
      });
      
      this.newMessage = '';
    }
  }
}
