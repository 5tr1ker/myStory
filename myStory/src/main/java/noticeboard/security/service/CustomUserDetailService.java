package noticeboard.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import noticeboard.entity.userdata.IdInfo;
import noticeboard.repository.LoginRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired LoginRepository loginRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		IdInfo result = loginRepository.findById(username);
		if(result == null) new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		
		return result;
    }

	
}
