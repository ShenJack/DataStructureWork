// ImageDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Image.h"
#include "ImageDlg.h"

#include "atlimage.h"
#include <math.h>
#include <string>
#include<iostream>
using namespace std;
CString gImgPath;
CImage MyImage;
CImage NewImage;
CImage SimilarImage1;
CImage SimilarImage2;
CImage SimilarImage3;

long long fingerprint[100];//存储所有图像库中图像的指纹
CString imagename[100];

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

BOOL imgPosition=TRUE;
BOOL imgPosition2=TRUE;
// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}	

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// CImageDlg dialog




CImageDlg::CImageDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CImageDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CImageDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CImageDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_open, &CImageDlg::OnBnClickedopen)
	ON_BN_CLICKED(IDC_PROCESS, &CImageDlg::OnBnClickedProcess)
	ON_BN_CLICKED(IDC_BUTTON1, &CImageDlg::OnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CImageDlg::OnClickedButton2)
END_MESSAGE_MAP()


// CImageDlg message handlers

BOOL CImageDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CImageDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CImageDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CRect lImg(0,0,380,380);
		CPaintDC dc(this); // 用于绘制的设备上下文
		if(gImgPath.IsEmpty())
			dc.FillSolidRect(&lImg, RGB(255,255,255));
		else 
		{
			MyImage.Draw(dc.m_hDC,lImg.left,lImg.top,lImg.Width(),lImg.Height());
		}
		
		CRect rImg(420,0,800,380);
		//CPaintDC dcc(this); // 只需一个
		if(imgPosition)  // imgPosition 判断依据
			dc.FillSolidRect(&rImg, RGB(255,255,255));
		else
		{
			NewImage.Draw(dc.m_hDC,rImg.left,rImg.top,rImg.Width(),rImg.Height());
		}

		CRect tImg(0,400,380,780);
		//CPaintDC dcc(this); // 只需一个
		if(imgPosition2)  // imgPosition 判断依据
			dc.FillSolidRect(&tImg, RGB(255,255,255));
		else
		{
			SimilarImage1.Draw(dc.m_hDC,rImg.left,rImg.top,rImg.Width(),rImg.Height());
		}




		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CImageDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}


void CImageDlg::OnBnClickedopen()
{
	imgPosition=TRUE;
	//选择图像文件
	CString   sFilter=_T("图像文件(jpg,Bmp)|*.jpg;*.jpeg;*.bmp||");
	CFileDialog dlg(TRUE,NULL,NULL,OFN_HIDEREADONLY|OFN_FILEMUSTEXIST|OFN_ENABLESIZING,sFilter);
	if(dlg.DoModal() == IDOK)
	{
		gImgPath = dlg.GetPathName();
		MyImage.Destroy();
		MyImage.Load(gImgPath);
		
		NewImage.Destroy();//--
		NewImage.Load(gImgPath);//--这里很关键
		Invalidate();
	}
}

void CImageDlg::OnBnClickedProcess()
{
	//  处理函数
	if(MyImage.IsNull())
	{
		MessageBox(_T("请先打开一幅图像！"),_T("提示"),MB_OK+MB_ICONWARNING);
		return;
	}
	imgPosition=FALSE;
	CWaitCursor WaitCursor;
	
	int Height = MyImage.GetHeight();
	int Width = MyImage.GetWidth();
	int bitcount=MyImage.GetBPP();//记录图像每个像素占据的位数

	/*//或者
	int Height = NewImage.GetHeight();
	int Width = NewImage.GetWidth();*/
	int i=0;
	if(bitcount==8)
	{ 
		NewImage=MyImage;
	}
	else
	{
		if(bitcount==24)
		{
			
			//灰度化
			for(int x=0; x<Width; x++)
	      {
		     for(int y=0; y<Height; y++)
		   {
			 COLORREF pixel = MyImage.GetPixel(x,y);
			  byte r,g,b;
			  r=GetRValue(pixel);
			  g=GetGValue(pixel);
			  b=GetBValue(pixel);
			  float  ave=(r+g+b)/3;
			  int newvalue=floor(ave);

			  NewImage.SetPixelRGB(x,y,newvalue,newvalue,newvalue);//设置新图像为灰度图像
			
			 }
			}
		}
	}


	for(int x=0; x<Width; x++)
	{
		for(int y=0; y<Height; y++)
		{
			COLORREF pixel = MyImage.GetPixel(x,y);//或者COLORREF pixel = NewImage.GetPixel(x,y);
			NewImage.SetPixel(x,y,pixel);
			byte r,g,b;
			r=GetRValue(pixel);
			g=GetGValue(pixel);
			b=GetBValue(pixel);
			NewImage.SetPixelRGB(x, y, 255 - r, 255 - g, 255 - b);//反色;
		}	
	}
	Invalidate();
	
}


void CImageDlg::OnClickedButton1()
{
	// TODO: Add your control notification handler code here

		//  处理函数
	if(NewImage.IsNull())
	{
		MessageBox(_T("请先打开一幅图像！"),_T("提示"),MB_OK+MB_ICONWARNING);
		return;
	}
	imgPosition=FALSE;
	CWaitCursor WaitCursor;
	
	int Height = NewImage.GetHeight();
	int Width = NewImage.GetWidth();
	int bitcount=NewImage.GetBPP();//记录图像每个像素占据的位数

	
	//缩小图像
	//int dstx=240;
	//int dsty=240;

	int dstx=8;
	int dsty=8;

	int blockx=Width/dstx;
	int blocky=Height/dsty;

	float sum=0;
	int ave=0;
	int px,py;//记录图像坐标
	//int smallImg[240][240];
		int smallImg[8][8];
		 for(int x=0; x<dstx; x++)
	      {
		     for(int y=0; y<dsty; y++)
		   {
			   sum=0;
			   ave=0;
			   for(int i=1;i<=blockx;i++)
				   for(int j=1;j<=blocky;j++)
				   {
					   px=x*blockx+i-1;
					   py=y*blocky+j-1;


			            COLORREF pixel = NewImage.GetPixel(px,py);
			            byte r;
			            r=GetRValue(pixel);
			           sum=sum+r;
				   }
			    ave=sum/(blockx*blocky);
				smallImg[x][y]=ave;
			 }
			}
		
		 //显示缩小了的图像
			 for (int x=0;x<dstx;x++)
				 for(int y=0;y<dsty;y++)
				 {
		       
					NewImage.SetPixelRGB(x,y,smallImg[x][y],smallImg[x][y],smallImg[x][y]);//设置新图像为灰度图像
				 }
		
		for(int x=dstx;x<Width;x++)
			for(int y=0;y<Height;y++)
				 NewImage.SetPixelRGB(x,y,255,255,255);
		for(int x=0;x<Width;x++)
			for(int y=dsty;y<Height;y++)
				 NewImage.SetPixelRGB(x,y,255,255,255);

		Invalidate();
		//计算得到图像的指纹
		//量化
		//int range=4;
		//int oldvalue=0;
		//int newvalue=0;
		//sum=0;
		//ave=0;
		//for (int x=0;x<dstx;x++)
		//	 for(int y=0;y<dsty;y++)
		//	 {
		//		oldvalue=smallImg[x][y];
		//		newvalue=oldvalue/range;
		//		smallImg[x][y]=newvalue;
		//		sum=sum+newvalue;
		//	 }

		//	 ave=sum/(dstx*dsty);

		////hash值
  //       for (int x=0;x<dstx;x++)
		//	 for(int y=0;y<dsty;y++)
		//	 {
		//		 if(smallImg[x][y]>=ave)
		//			 smallImg[x][y]=1;
		//		 else
		//			 smallImg[x][y]=0;
		//	 }

		//	 //显示二值的hash矩阵.0用黑色（0，0，0）显示，1用白色（255，255，255）显示。

		//	 
		//for (int x=0;x<dstx;x++)
		//	 for(int y=0;y<dsty;y++)
		//	 {
		//       if(smallImg[x][y]==1)
  //                NewImage.SetPixelRGB(x,y,255,255,255);//设置新图像为灰度图像
		//	   else
		//		   NewImage.SetPixelRGB(x,y,0,0,0);

		//	 }
		//
		//for(int x=dstx;x<Width;x++)
		//	for(int y=0;y<Height;y++)
		//		 NewImage.SetPixelRGB(x,y,255,255,255);
		//for(int x=0;x<Width;x++)
		//	for(int y=dsty;y<Height;y++)
		//		 NewImage.SetPixelRGB(x,y,255,255,255);


	
		//	Invalidate();
	



}


bool CImageDlg::initialImageSet(long double fingerprint)
{

	return false;
}


void CImageDlg::OnClickedButton2()
{
	//打开指定位置的所有图像，提取图像指纹，建立图像库的查找表
	int i,j;
	CString s1,s2,s3,s4,filename;
	s1="..\\imageset\\";
	s2="00";
	s4=".bmp";
	for(i=0;i<28;i++)
	{
		MyImage.Destroy();

		if(i>=10)
		{
		  s2="0";
		}
		s3.Format(_T("%d"),i+1);//转换整型为字符串型
	    filename=s1+s2+s3+s4;
		MyImage.Load(filename);
		imagename[i]=filename;
		
		//灰度化

		
	   //缩小


		//得到指纹


		//建立有序顺序表，二叉排序树，hash表



		
	/*	gImgPath=filename;//显示该图像
		
        if(MyImage.IsNull())
	   {
		MessageBox(_T("请先打开一幅图像！"),_T("提示"),MB_OK+MB_ICONWARNING);
		return;
	    }
 

		Invalidate();*/
	

	}

	


	
	// TODO: Add your control notification handler code here
}
