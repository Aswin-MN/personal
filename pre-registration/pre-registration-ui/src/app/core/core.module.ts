import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AboutUsComponent } from './about-us/about-us.component';
import { FaqComponent } from './faq/faq.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ContactComponent } from './contact/contact.component';
import { AppRoutingModule } from '../app-routing.module';
import { MaterialModule } from '../material.module';
import { I18nModule } from '../i18n.module';

@NgModule({
  imports: [CommonModule, AppRoutingModule, MaterialModule, I18nModule],
  declarations: [HeaderComponent, FooterComponent, AboutUsComponent, FaqComponent, ContactComponent],
  exports: [HeaderComponent, FooterComponent]
})
export class CoreModule {}
