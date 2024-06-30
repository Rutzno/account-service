package accountservice.service;

import account.entities.MyUser;
import account.entities.MyUserAdapter;
import account.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.2
 */

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    private final MyUserRepository myUserRepository;

    @Autowired
    public MyUserDetailsServiceImpl(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(username +" not found"));
        return new MyUserAdapter(myUser);
    }
}