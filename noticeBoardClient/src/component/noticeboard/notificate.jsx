import axios from "axios";
import React , { Fragment , useEffect , useState } from "react";

const Notificate = ({dropDownSet , userId , notifiedMode}) => {

    const [list , setList] = useState([]);

    const connectUrl = (postNumber) => {
        window.location.href = `/viewPost?page=${postNumber - 1}`;
    }
    
    const Notifycontent = ({data}) => {
        return (data.map(item => (
        <div key={item.id} className="notificateitem" onClick={() => connectUrl(item.postNumber)}>
            <header className="nfitemheader">
                <span id="headerNamed">{item.writer}</span>님이 댓글을 달았습니다.</header>
            <div className="nfitemcontent">
                {item.content}
            </div>
            <footer className="nfitemfooter">{item.postType} / {item.postTime}</footer>
        </div>
        )));
    }

    useEffect(async() => {
        const result = await axios({
            url : `/getNotifice/${userId}` ,
            method : "GET" ,
            mode : "cors"
        });
        setList(result.data);
    } , []);

    return(
        <Fragment>
            <div className="notificateFrame">
                <header className="notificateheader">
                    <span>Notification</span>
                    <svg xmlns="http://www.w3.org/2000/svg" onClick={() => dropDownSet(false)} width="25" height="25" fill="currentColor" className="bi bi-x-lg closenotc" viewBox="0 0 16 16">
                        <path fillRule="evenodd" d="M13.854 2.146a.5.5 0 0 1 0 .708l-11 11a.5.5 0 0 1-.708-.708l11-11a.5.5 0 0 1 .708 0Z" />
                        <path fillRule="evenodd" d="M2.146 2.146a.5.5 0 0 0 0 .708l11 11a.5.5 0 0 0 .708-.708l-11-11a.5.5 0 0 0-.708 0Z" />
                    </svg>
                </header>
                <section className="notificatesection">
                    <Notifycontent data={notifiedMode === 4 ? list : []}/> {/* 알람 설정에 따라 노출 설정 */}
                </section>
                <footer className="notificatefooter"><svg xmlns="http://www.w3.org/2000/svg"  onClick={() => window.location.href = '/profile'} width="20" height="20" fill="currentColor" className="bi bi-gear" viewBox="0 0 16 16">
                    <path d="M8 4.754a3.246 3.246 0 1 0 0 6.492 3.246 3.246 0 0 0 0-6.492zM5.754 8a2.246 2.246 0 1 1 4.492 0 2.246 2.246 0 0 1-4.492 0z" />
                    <path d="M9.796 1.343c-.527-1.79-3.065-1.79-3.592 0l-.094.319a.873.873 0 0 1-1.255.52l-.292-.16c-1.64-.892-3.433.902-2.54 2.541l.159.292a.873.873 0 0 1-.52 1.255l-.319.094c-1.79.527-1.79 3.065 0 3.592l.319.094a.873.873 0 0 1 .52 1.255l-.16.292c-.892 1.64.901 3.434 2.541 2.54l.292-.159a.873.873 0 0 1 1.255.52l.094.319c.527 1.79 3.065 1.79 3.592 0l.094-.319a.873.873 0 0 1 1.255-.52l.292.16c1.64.893 3.434-.902 2.54-2.541l-.159-.292a.873.873 0 0 1 .52-1.255l.319-.094c1.79-.527 1.79-3.065 0-3.592l-.319-.094a.873.873 0 0 1-.52-1.255l.16-.292c.893-1.64-.902-3.433-2.541-2.54l-.292.159a.873.873 0 0 1-1.255-.52l-.094-.319zm-2.633.283c.246-.835 1.428-.835 1.674 0l.094.319a1.873 1.873 0 0 0 2.693 1.115l.291-.16c.764-.415 1.6.42 1.184 1.185l-.159.292a1.873 1.873 0 0 0 1.116 2.692l.318.094c.835.246.835 1.428 0 1.674l-.319.094a1.873 1.873 0 0 0-1.115 2.693l.16.291c.415.764-.42 1.6-1.185 1.184l-.291-.159a1.873 1.873 0 0 0-2.693 1.116l-.094.318c-.246.835-1.428.835-1.674 0l-.094-.319a1.873 1.873 0 0 0-2.692-1.115l-.292.16c-.764.415-1.6-.42-1.184-1.185l.159-.291A1.873 1.873 0 0 0 1.945 8.93l-.319-.094c-.835-.246-.835-1.428 0-1.674l.319-.094A1.873 1.873 0 0 0 3.06 4.377l-.16-.292c-.415-.764.42-1.6 1.185-1.184l.292.159a1.873 1.873 0 0 0 2.692-1.115l.094-.319z" />
                </svg>
                <span  onClick={() => window.location.href = '/profile'}>알림 설정</span>
                </footer>
            </div>
        </Fragment>
    )
}

export default React.memo(Notificate);