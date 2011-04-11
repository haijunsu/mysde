/***************************
*                          *
*   Name:   dropDownMenu   *
*   Author: Aken li        *
*   Date:   2008.6.25      *
*   BLOG:   www.kxbd.com   *
*                          *
***************************/

var dropDownMenu = Class.create();
dropDownMenu.prototype = {
	initialize:function(menu,option){
		this.menu = menu;
		this.option = Object.extend({dur:0.3,type:0,delayDur:0.2}, option || {});
		this.dur = this.option.dur;
		this.type = this.option.type;
		this.delayDur = this.option.delayDur;
		this.menuNow = [];
		this.navDelayID = [];
		this.menuDelayID = [];
		this.init();
	},

	init:function(){
		for(var i = 0; i< this.menu.length; i++){
			for (var j = 0; j < this.menu[i].length ;j++ ){
				this.menuRender(this.menu[i][j],i);//菜单位置初始化，设置遮罩
			}
		}
		if(this.type == 2){
			for(var i = 0; i< this.menu.length; i++){
				for (var j = 0; j < this.menu[i].length ;j++ ){
					var menu = $(this.menu[i][j]+"0")
					var menu_m = menu.down();
					menu_m.setStyle({position: "absolute", bottom:menu.getDimensions().height+"px"});
				}
			}
		}
	},

	menuRender:function(id,level){
		var nav = $(id);
		var dim_nav = nav.getDimensions();
		var pos_nav = nav.viewportOffset();
		var menu = $(id+"0");
		var dim_menu = menu.getDimensions();
		if(level==0){
			menu.setStyle({left:pos_nav.left+"px",top:pos_nav.top+dim_nav.height+"px",height:"0px"}).makeClipping();
		}else{
			menu.setStyle({left:pos_nav.left+dim_nav.width+"px",top:pos_nav.top+"px",height:"0px"}).makeClipping();
		}
		nav.observe("mouseover",function(e){
			if(this.menuNow[level]==menu){
				window.clearTimeout(this.menuDelayID[level]);
			}
			/*
			if(this.menuNow[level]&&this.menuNow[level]!=menu){
				this.mvHideMenu(this.menuNow[level],this.dur,this.type,this.menuNow[level].getHeight());
			}
			*/
			this.mvShowMenu(menu,this.dur,this.type,dim_menu.height);
			this.menuNow.length = level;
			this.menuNow[level]=menu;
		}.bind(this));
		nav.observe("mouseout",function(e){
			this.navDelayID[level] = this.mvHideMenu.delay(this.delayDur,menu,this.dur,this.type,dim_menu.height);
		}.bind(this));
		menu.observe("mouseover",function(e){
			this.navDelayID.each(function(o){
				window.clearTimeout(o);
			})
			var i = level;
			while (i>=0)
			{
				window.clearTimeout(this.menuDelayID[i]);
				i--;
			}
		}.bind(this));
		menu.observe("mouseout",function(e){
			this.menuNow.each(function(o,i){
				this.menuDelayID[i] = this.mvHideMenu.delay(this.delayDur,o,this.dur,this.type,o.getDimensions().height);
			}.bind(this));
		}.bind(this));
	},

	mvShowMenu:function(element,dur,changeType,h){
		var o = $(element);
		var dur = dur;
		var type = changeType;
		var oH = h;
		new Effect.Morph(o, {
			style: 'height:'+oH+'px',
			transition:Effect.Transitions.linear,
			afterUpdateInternal: function(effect) {
				if (type == 2){
					effect.element.down().setStyle({bottom:(oH - effect.element.clientHeight) + 'px' });
				}else if(type == 1){
					effect.element.setStyle({opacity: effect.element.clientHeight/oH });
				}
			},
			duration:dur
		});
		return element;
	},

	mvHideMenu:function(element,dur,changeType,h){
		var o = $(element);
		var dur = dur;
		var oH = h;
		var type = changeType;

		new Effect.Morph(o, {
			style: 'height:0px',
			transition:Effect.Transitions.linear,
			afterUpdateInternal: function(effect) {
				if (type == 2){
					effect.element.down().setStyle({bottom:(oH - effect.element.clientHeight) + 'px' });
				}else if(type == 1){
					effect.element.setStyle({opacity: effect.element.clientHeight/oH });
				}
			},
			duration:dur
		});

		return element;
	}
};
var menuContent = [
	["nav1","nav2","nav3","nav4","nav5"],
	["nav12","nav21","nav23","nav43"],
	["nav122"],
	["nav1222"]

]