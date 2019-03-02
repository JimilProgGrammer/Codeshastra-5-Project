import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareSummaryComponent } from './share-summary.component';

describe('ShareSummaryComponent', () => {
  let component: ShareSummaryComponent;
  let fixture: ComponentFixture<ShareSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShareSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShareSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
